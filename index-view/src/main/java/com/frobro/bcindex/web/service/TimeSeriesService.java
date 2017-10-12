package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.query.IndexUpdate;
import com.frobro.bcindex.web.service.query.TimeSeriesQuery;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 5/12/17.
 */
public class TimeSeriesService {

  private static final BcLog log = BcLog.getLogger(TimeSeriesService.class);
  private static final int UNIT_HOURLY = 60;
  private static final int UNIT_DAILY = UNIT_HOURLY *24;
  private static final int UNIT_WEEKLY = UNIT_DAILY * 7;
  private static final int UNIT_MONTHLY = UNIT_DAILY * 30;

  private int numPoints = 200;
  private JdbcTemplate jdbc;

  public void setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public int getNumPoints() {
    return numPoints;
  }

  public ApiResponse getData(RequestDto req) {
    ApiResponse response = getResponse(req);
    return response;
  }

  private ApiResponse getResponse(RequestDto req) {
    TimeFrame timeFrame = req.timeFrame;
    TimeSeriesQuery query = timeFrame.getQuery(req);

    ApiResponse response = ApiResponse.newResponse(req);

    // populate the response
    query.execute(jdbc, response);

    response.calcAndFormatData();
    return response;
  }

  public GroupUpdate getLastestData() {
    return null;
  }
}
