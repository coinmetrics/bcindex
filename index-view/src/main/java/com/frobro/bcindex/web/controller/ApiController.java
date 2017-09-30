package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.cache.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by rise on 5/12/17.
 */
@RestController
public class ApiController {
  private static final BcLog log = BcLog.getLogger(ApiController.class);

  private DbTickerService dbTickerService = new DbTickerService();
  private final DataCache cache = new DataCache();
  private TimerService timerService;

  @Autowired
  public void init(JdbcTemplate jdbc) {
    dbTickerService.setJdbc(jdbc);
//    cache.populateFromDb(dbTickerService);
    timerService = new TimerService(dbTickerService);
    timerService.run();
  }

  @PostConstruct
  public void init(){
    timerService.updateIfInDevMode();
  }

  @RequestMapping(value = "api/index", method = RequestMethod.POST)
  public String publicApiEndPoint(@RequestBody String reqStr) {

    try {

      PublicRequest pubReq = RequestConverter.convert(reqStr);
      RequestDto dto = RequestConverter.convert(pubReq);
      ApiResponse privateResp = cache.respondTo(dto);
      PublicApiResponse publicResp = RequestConverter.convert(privateResp);
      return DbTickerService.toJson(publicResp);

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
