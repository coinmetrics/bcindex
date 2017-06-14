package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.frobro.bcindex.web.service.DoubleFormatter.format;

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

  private ApiResponse getresponseForAll(RequestDto req) {
    String table = getIdxColName(req.index);
    String currency = getCurrColName(req.currency);
    String query = "";
    ApiResponse response = createResponse(req);
    response.addPrice(100.0)
        .addTime("2017-05-17 23:53:31.16");

    return response;
  }

  private ApiResponse getResponse(RequestDto req) {
    int numOfPoints = req.timeFrame.getNumDataPoints();
    int numBack = req.timeFrame.getTimeStep();

    int oldestRecOfInterest = numOfPoints;

    String table = getIdxColName(req.index);
    String currency = getCurrColName(req.currency);
    String query = "select last." + currency + " as lastpx, b." + currency
      + ", b.time_stamp" + " from "
      + "(select " + currency + " from " + table
      + " where id = (select count(*) from " + table + ")) as last,"
      + "(select " + currency
      + ", time_stamp from " + table + " where id > "
      + lastIdOfInterest(oldestRecOfInterest, table)
      + " and "
      + "(MOD(id," + numBack + ") = 0) "
      + "order by id) as b;";

    System.out.println(query);
    ApiResponse response = createResponse(req);
    
        jdbc.query(query, (rs, rowNum) ->
              response.addPrice(format(rs.getDouble(currency)))
                      .addTime(rs.getString("time_stamp"))
                      .updateLast(rs.getDouble("lastpx"))
        );

    response.calculateDerivedData();
    return response;
  }

  private ApiResponse createResponse(RequestDto req) {
    ApiResponse response = new ApiResponse();
    response.currency = req.currency;
    response.index = req.index;
    response.timeFrame = req.timeFrame;
    response.timeUnit = req.timeFrame.getTimeStepUnit();
    return response;
  }

  private String getIdxColName(IndexType type) {
    if (type == IndexType.TEN_EVEN) {
      return "even_index";
    }
    else if (type == IndexType.TEN_IDX) {
      return "odd_index";
    }
    else {
      throw new IllegalStateException("no table name exists for index type: " + type);
    }
  }

  private String getCurrColName(Currency currency) {
    if (currency == Currency.BTC) {
      return "index_value_btc";
    }
    else if (currency == Currency.USD) {
      return "index_value_usd";
    }
    else {
      throw new IllegalStateException("no column name exists for currency: " + currency);
    }
  }

  private String lastIdOfInterest(int numRecords, String table) {
    return " (select count(*) from " + table + ") - " + numRecords;
  }
}
