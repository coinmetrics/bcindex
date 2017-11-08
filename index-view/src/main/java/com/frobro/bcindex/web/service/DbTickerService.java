package com.frobro.bcindex.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 3/23/17.
 */
public class DbTickerService implements DataProvider {

  private static final BcLog log = BcLog.getLogger(DbTickerService.class);
  private TimeSeriesService timeSeriesService = new TimeSeriesService();
  private JdbcTemplate jdbc;

  public static String toJson(Object response) {
    String responseStr = "error";
    try {

      responseStr = new ObjectMapper().writeValueAsString(response);

    } catch (JsonProcessingException jpe) {
      log.error(jpe);
    }
    return responseStr;
  }

  public DbTickerService setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
    timeSeriesService.setJdbc(jdbc);
    return this;
  }

  public String respondAsJson(RequestDto req) {
    return toJson(getData(req));
  }

  @Override
  public ApiResponse getData(RequestDto req) {
    return timeSeriesService.getData(req);
  }

  public GroupUpdate getUpdate() {
    return timeSeriesService.getLastestData();
  }
}

