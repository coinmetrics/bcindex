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
  protected static final String LAST_TIME_COL = "lasttime";
  protected static final String FIRST_TIME_COL = "firsttime";
  protected static final String FIRST_PX_COL = "firstpx";
  protected static final String MAX_PRICE = "maxprice";
  protected static final String MIN_PRICE = "minprice";

  private final RequestDto req;
  protected final String currency;
  protected final String table;

  public TimeSeriesQuery(RequestDto req) {
    this.req = req;
    this.currency = getCurrColName(req.currency);
    table = getTableName(req.index);
  }

  public String getCurrency() {
    return currency;
  }

  public ApiResponse execute(JdbcTemplate jdbc, ApiResponse response) {
    jdbc.query(queryString(), (rs, rowNum) ->
            response.addData(rs.getDouble(getCurrency()), rs.getLong(TimeSeriesQuery.TIME_COL))
                    .updateLast(rs.getDouble(LAST_PX_COL), rs.getLong(LAST_TIME_COL))
                    .updateFirst(rs.getDouble(FIRST_PX_COL), rs.getLong(FIRST_TIME_COL))
                    .setMaxAndMinPrice(rs.getDouble(MAX_PRICE), rs.getDouble(MIN_PRICE))
    );
    return response;
  }

  public String queryString() {
    int numOfPoints = req.timeFrame.getNumDataPoints();
    int numBack = req.timeFrame.getTimeStep();

    String table = getTableName(req.index);
    String idForTimeFrame = lastIdOfInterest(numOfPoints, table);

    String query = "select last." + currency + " as "
        + LAST_PX_COL
        + ", last." + TIME_COL + " as " + LAST_TIME_COL
        + ", first." + currency + " as " + FIRST_PX_COL
        + ", first." + TIME_COL + " as " + FIRST_TIME_COL + ", b." + currency
        + ", b." + TIME_COL + ", prices.max as " + MAX_PRICE + ", prices.min "
        + " as " + MIN_PRICE
        + " from "
        + "(select max(" + currency + ") as max, min(" + currency + ") as min "
        + " from " + table + " where bletch_id > "
        + idForTimeFrame + " ) as prices, "
        + "(select " + currency + ", " + TIME_COL + " from " + table
        + " where bletch_id = (select max(bletch_id) from " + table + ")) as last,"
        + "(select " + currency + ", " + TIME_COL + " from " + table
        + " where bletch_id = (select min(bletch_id) from " + table + " where bletch_id > "
        + idForTimeFrame + ")) as first,"
        + "(select " + currency
        + ", " + TIME_COL + " from " + table + " where bletch_id > "
        + idForTimeFrame
        + " and "
        + "(MOD(bletch_id," + numBack + ") = 0) "
        + "order by bletch_id) as b;";

    return query;
  }

  protected String getTableName(IndexType type) {
    if (type == IndexType.EVEN_INDEX) {
      return "even_index";
    }
    else if (type == IndexType.ODD_INDEX) {
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
