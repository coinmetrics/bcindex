package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rise on 5/12/17.
 */
public class TimeSeriesService {

  private static final BcLog log = BcLog.getLogger(TimeSeriesService.class);
  private static final int UNIT_HOURLY = 60;
  private static final int UNIT_DAILY = UNIT_HOURLY *24;
  private static final int UNIT_WEEKLY = UNIT_DAILY * 7;
  private static final int UNIT_MONTHLY = UNIT_DAILY * 30;

  private int numPoints = 3;
  private JdbcTemplate jdbc;

  public void setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public int getNumPoints() {
    return numPoints;
  }

  public ApiResponse getData(RequestDto req) {
    long now = Instant.now().toEpochMilli();
    ApiResponse response;

    switch(req.timeFrame) {
      case HOURLY:
        response = getResponse(req, now, UNIT_HOURLY);
        break;
      case DAILY:
        response = getResponse(req, now, UNIT_DAILY);
        break;
      case WEEKLY:
        response = getResponse(req, now, UNIT_WEEKLY);
        break;
      case MONTHLY:
        response = getResponse(req, now, UNIT_MONTHLY);
        break;
      default:
        response = getResponse(req, now, UNIT_DAILY);
        break;
    }
    return response;
  }

  private ApiResponse getResponse(RequestDto req, long now, int timeUnit) {
    int oldestRecOfInterest = numPoints * timeUnit;

    String table = getIdxColName(req.index);
    String currency = getCurrColName(req.currency);
    String query = "select " + currency
      + ", time_stamp from " + table + " where id > "
      + lastIdOfInterest(oldestRecOfInterest, table)
      + " and "
      + "(MOD(id," + timeUnit + ") = 0) "
      + ";";

    ApiResponse response = createResponse(req);

        jdbc.query(query, (rs, rowNum) ->
              response.addPrice(rs.getDouble("index_value_usd"))
                      .addTime(rs.getString("time_stamp"))
        );

    response.setLastFromList();
    return response;
  }

  private ApiResponse createResponse(RequestDto req) {
    ApiResponse response = new ApiResponse();
    response.currency = req.currency;
    response.index = req.index;
    response.timeUnit = req.timeFrame;
    return response;
  }

  private String getIdxColName(IndexType type) {
    if (type == IndexType.EVEN) {
      return "even_index";
    }
    else if (type == IndexType.ODD) {
      return "odd_index";
    }
    else {
      throw new IllegalStateException("no table name exists for index type: " + type);
    }
  }

  private String getCurrColName(Currency currency) {
    if (currency == Currency.BIT_COIN) {
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
