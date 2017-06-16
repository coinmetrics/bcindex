package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;

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

  public String asString() {
    int numOfPoints = req.timeFrame.getNumDataPoints();
    int numBack = req.timeFrame.getTimeStep();

    String table = getIdxColName(req.index);
    int oldestRecOfInterest = numOfPoints;

    String query = "select " + COUNT_COL + " last." + currency + " as "
        + LAST_PX_COL + ", b." + currency
        + ", b." + TIME_COL
        + " from "
        + "select count(*) from " + table + " as " + COUNT_COL + ", "
        + "(select " + currency + " from " + table
        + " where id = (select count(*) from " + table + ")) as last,"
        + "(select " + currency
        + ", " + TIME_COL + " from " + table + " where id > "
        + lastIdOfInterest(oldestRecOfInterest, table)
        + " and "
        + "(MOD(id," + numBack + ") = 0) "
        + "order by id) as b;";

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
    return " (select count(*) from " + table + ") - " + numRecords;
  }
}
