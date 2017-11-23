package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.cache.CacheLoader;
import com.frobro.bcindex.web.service.cache.CacheUpdateMgr;
import com.frobro.bcindex.web.service.cache.DataCache;
import com.frobro.bcindex.web.service.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/12/17.
 */
@RestController
public class ApiController {
  private static final BcLog log = BcLog.getLogger(ApiController.class);

  private DbTickerService dbTickerService = new DbTickerService();
  private TimerService timerService;
  private CacheUpdateMgr cacheMgr;
  private DataCache cache = new DataCache();
  private JdbcTemplate jdbc;
  private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  @Autowired
  public void init(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @PostConstruct
  public void init(){
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
    executor.schedule(task, 0, TimeUnit.SECONDS);

    // start data update thread
    timerService = new TimerService(cacheMgr);
    timerService.run(cacheLoader);
    timerService.updateIfInDevMode();
  }


  private Runnable getLoadCacheTask() {
    // cacheLoader.load needs to be called off the main thread because
    // it takes more than 90 seconds to load. Heroku will
    // raise a
    // Error R10 (Boot timeout) -> Web process failed to bind to $PORT within 90 seconds to launch
    // If the dynamo state is not "UP" in 75 seconds an H20 will be thrown, even if the port is bound in time
    return () -> {
      new CacheLoader(cache, cacheMgr, dbTickerService).load();
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
                + PublicIndex.ETHER_INDEX.name()
                + ", " + PublicIndex.EVEN_ETHER_INDEX.name()
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
