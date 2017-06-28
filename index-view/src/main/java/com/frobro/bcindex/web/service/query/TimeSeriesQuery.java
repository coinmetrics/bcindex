package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 6/15/17.
 */
public class TimeSeriesQuery {
  public static String LAST_PX_COL = "lastpx";
  public static String TIME_COL = "time_stamp";
  public static String COUNT_COL = "cnt";

  private final RequestDto req;
  protected final String currency;
  protected final String table;

  public TimeSeriesQuery(RequestDto req) {
    this.req = req;
    this.currency = getCurrColName(req.currency);
    table = getIdxColName(req.index);
  }

  public String getCurrency() {
    return currency;
  }

  public ApiResponse execute(JdbcTemplate jdbc, ApiResponse response) {
    jdbc.query(queryString(), (rs, rowNum) ->
            response.addPrice(rs.getDouble(getCurrency()))
                .addTime(rs.getLong(TimeSeriesQuery.TIME_COL))
                .updateLast(rs.getDouble(TimeSeriesQuery.LAST_PX_COL))
    );
    return response;
  }

  public String queryString() {
    int numOfPoints = req.timeFrame.getNumDataPoints();
    int numBack = req.timeFrame.getTimeStep();

    String table = getIdxColName(req.index);
    int oldestRecOfInterest = numOfPoints;

    String query = "select last." + currency + " as "
        + LAST_PX_COL + ", b." + currency
        + ", b." + TIME_COL
        + " from "
        + "(select " + currency + " from " + table
        + " where bletch_id = (select max(bletch_id) from " + table + ")) as last,"
        + "(select " + currency
        + ", " + TIME_COL + " from " + table + " where bletch_id > "
        + lastIdOfInterest(oldestRecOfInterest, table)
        + " and "
        + "(MOD(bletch_id," + numBack + ") = 0) "
        + "order by bletch_id) as b;";

    System.out.println();
    System.out.println(query);
    return query;
  }

  private String getIdxColName(IndexType type) {
    if (type == IndexType.EVEN) {
      return "even_index";
    }
    else if (type == IndexType.ODD) {
      return "odd_index";
    }
    else {
      return type.name();
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
    return " (select max(bletch_id) from " + table + ") - " + numRecords;
  }
}
