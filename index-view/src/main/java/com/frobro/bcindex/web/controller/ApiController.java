package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

/**
 * Created by rise on 5/12/17.
 */
@RestController
public class ApiController {
  private static final BcLog log = BcLog.getLogger(ApiController.class);

  private DbTickerService dbTickerService = new DbTickerService();
  private TimerService timerService;

  @Autowired
  public void init(JdbcTemplate jdbc) {
    dbTickerService.setJdbc(jdbc);
    timerService = new TimerService(dbTickerService);
    timerService.run();
  }

  @PostConstruct
  public void init(){
    timerService.updateIfInDevMode();
  }

  @RequestMapping(value = "api/index", method = RequestMethod.POST)
  public String getIndexEndPoint(@RequestBody @Valid RequestDto dto) {
    return getIndex(dto);
  }

  @RequestMapping(value = "api/index", method = RequestMethod.GET)
  public String getIndex(@RequestParam(required = false) String timeUnit) {
    return getIndex(getDefaultDto());
  }

  private String getIndex(RequestDto dto) {
    if (reqestNotValid(dto)) {
      dto = getDefaultDto();
      log.error("request to api is not valid, using default");
    }

    return dbTickerService.respondAsJson(dto);
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
    return getDefaultDto(TimeFrame.HOURLY.name());
  }

  private RequestDto getDefaultDto(String timeUnit) {
    RequestDto dto = new RequestDto();
    dto.timeFrame = parseTimeUnit(timeUnit);
    dto.currency = Currency.USD;
    dto.index = IndexType.ODD;
    return dto;
  }

  private TimeFrame parseTimeUnit(String unit) {
    TimeFrame frame = TimeFrame.HOURLY;

    if (unit == null) {
      return frame;
    }
    switch (unit) {
      case "all":
        frame = TimeFrame.YEARLY;
        break;
      case "hourly":
        frame = TimeFrame.HOURLY;
        break;
    }
    return frame;
  }
}
