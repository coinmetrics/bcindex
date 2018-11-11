package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.query.GroupUpdateQuery;
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

  private JdbcTemplate jdbc;

  public void setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
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

    if (response.firstAndLastNotNull()) {
      response.calcAndFormatData();
    }
    else {
      log.error("not complete data for " + req);
    }
    return response;
  }

  public GroupUpdate getLastestData() {
    GroupUpdate update = new GroupUpdate();
    GroupUpdateQuery query = new GroupUpdateQuery();
    query.execute(jdbc, update);

    return update;
  }
}
