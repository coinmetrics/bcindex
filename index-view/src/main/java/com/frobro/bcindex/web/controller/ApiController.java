package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.cache.CacheLoader;
import com.frobro.bcindex.web.service.cache.CacheUpdateMgr;
import com.frobro.bcindex.web.service.cache.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by rise on 5/12/17.
 */
@RestController
public class ApiController {
  private static final String CONTROLLER_OFF = "bletch.controller.off";
  private static final BcLog log = BcLog.getLogger(ApiController.class);

  private DbTickerService dbTickerService = new DbTickerService();
  private TimerService timerService;
  private CacheUpdateMgr cacheMgr;
  private DataCache cache = new DataCache();
  private JdbcTemplate jdbc;
  private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  public static void turnOffThisControllerForTesting() {
    System.setProperty(CONTROLLER_OFF,"true");
  }

  private boolean thisControllerIsOn() {
    return System.getProperty(CONTROLLER_OFF) == null;
  }

  @Autowired
  public void init(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @PostConstruct
  public void init(){
    if (thisControllerIsOn()) {
      // init the db access
      dbTickerService.setJdbc(jdbc);
      TimeSeriesService service = new TimeSeriesService();
      service.setJdbc(jdbc);

      // initialize the cache
      cacheMgr = new CacheUpdateMgr(cache, service);
      CacheLoader cacheLoader =
          new CacheLoader(cache,cacheMgr, dbTickerService);

      // start cache loading thread
      Runnable task = getLoadCacheTask();
      Thread loadThread = new Thread(task);
      loadThread.start();

      // start data update thread
      timerService = new TimerService(cacheMgr);
      timerService.run(cacheLoader);
    }
  }


  private Runnable getLoadCacheTask() {
    // cacheLoader.load needs to be called off the main thread because
    // it takes more than 90 seconds to load. Heroku will
    // raise a
    // Error R10 (Boot timeout) -> Web process failed to bind to $PORT within 90 seconds to launch
    // If the dynamo state is not "UP" in 75 seconds an H20 will be thrown, even if the port is bound in time
    return () -> {
      try {
        new CacheLoader(cache, cacheMgr, dbTickerService).load();
      } catch (Exception e) {
        log.error(e);
      }
    };
  }

  @RequestMapping(value = "cacheupdate", method = RequestMethod.GET)
  public String cacheUpdate() {
    cacheMgr.update();
    return "updated";
  }

  @RequestMapping(value = "api/index", method = RequestMethod.POST)
  public String publicApiEndPoint(@RequestBody String reqStr) {

    try {

      PublicRequest pubReq = RequestConverter.convert(reqStr);
      RequestDto dto = RequestConverter.convert(pubReq);
      return processRequest(dto);

    } catch (IOException ioe) {
      log.error("error parsing api request:\n", ioe);
    }

    return DbTickerService.toJson(
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("supported currency ("
                + Currency.USD.name()
                + ", " + Currency.BTC
                + ") supported indexes ("
                + PublicIndex.FORTY_INDEX.name()
                + ", " + PublicIndex.FORTY_EVEN_INDEX.name()
                + ", "+ PublicIndex.ETHER_INDEX.name()
                + ", " + PublicIndex.EVEN_ETHER_INDEX.name()
                + ", "+ PublicIndex.TOTAL_INDEX.name()
                + ", " + PublicIndex.TOTAL_EVEN_INDEX.name()
                + ", "+ PublicIndex.CURRENCY_INDEX.name()
                + ", " + PublicIndex.PLATFORM_INDEX.name()
                + ", " + PublicIndex.APPLICATION_INDEX.name()
                + ") supported time frames ("
                + PublicTimeFrame.DAILY.name()
                + ", " + PublicTimeFrame.WEEKLY + ")"));
  }

  private String processRequest(RequestDto req) {
    ApiResponse privateResp = dbTickerService.getData(req);
    PublicApiResponse publicResp = RequestConverter.convert(privateResp);
    return DbTickerService.toJson(publicResp);
  }

  @RequestMapping(value = "blet/index", method = RequestMethod.POST)
  public String getIndexEndPoint(@RequestBody @Valid RequestDto dto) {
    return getIndex(dto);
  }

  @RequestMapping(value = "blet/index", method = RequestMethod.GET)
  public String getIndex() {
    return getIndex(getDefaultDto());
  }

  private String getIndex(RequestDto dto) {
    if (reqestNotValid(dto)) {
      dto = getDefaultDto();
      log.error("request to api is not valid, using default");
    }

    return cache.respondAsJson(dto);
  }

  private boolean reqestNotValid(RequestDto dto) {
    boolean hasErrors = true;

    if (dto != null) {
      hasErrors = not(dto.reqValid());
    }
    return hasErrors;
  }

  private boolean not(boolean b) {
    return !b;
  }

  private RequestDto getDefaultDto() {
    return getDefaultDto(TimeFrame.DAILY);
  }

  private RequestDto getDefaultDto(TimeFrame timeFrame) {
    RequestDto dto = new RequestDto();
    dto.timeFrame = timeFrame;
    dto.currency = Currency.USD;
    dto.index = IndexType.ODD_INDEX;
    return dto;
  }
}
