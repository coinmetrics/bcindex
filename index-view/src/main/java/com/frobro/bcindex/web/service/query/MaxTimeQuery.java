package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.MaxApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 6/15/17.
 */
public class MaxTimeQuery extends TimeSeriesQuery {
  private static final String ENTRIES = "entries";
  private static final String NUM = "num";

  public MaxTimeQuery(RequestDto req) {
    super(req);
  }

  @Override
  public ApiResponse execute(JdbcTemplate jdbc, ApiResponse response) {
    MaxApiResponse maxResponse = (MaxApiResponse) response;

    jdbc.query(queryString(), (rs, rowNum) ->
            maxResponse
                .setTotalCount(rs.getLong(COUNT_COL))
                .addData(rs.getDouble(getCurrency()), rs.getLong(TimeSeriesQuery.TIME_COL))
                .updateLast(rs.getDouble(LAST_PX_COL), rs.getLong(LAST_TIME_COL))
                .updateFirst(rs.getDouble(FIRST_PX_COL), rs.getLong(FIRST_TIME_COL))
                .setMaxAndMinPrice(rs.getDouble(MAX_PRICE), rs.getDouble(MIN_PRICE))
    );
    return response;
  }

  @Override
  public String queryString() {

    String query = "select const.maxbid "
        + " as " + COUNT_COL + ", const.maxprice as " + MAX_PRICE + ", "
        + "const.minprice as " + MIN_PRICE + ", last." + currency + " as "
        + LAST_PX_COL
        + ", last." + TIME_COL + " as " + LAST_TIME_COL
        + ", first." + currency + " as " + FIRST_PX_COL
        + ", first." + TIME_COL + " as " + FIRST_TIME_COL + ", b." + currency
        + ", b." + TIME_COL
        + " from "
        + "(select max(bletch_id) as maxbid, max(" + currency + ") as maxprice, "
        + "min(" + currency + ") as minprice "
        + " from " + table + ") as const, "
        + "(select " + currency + ", " + TIME_COL + " from " + table
        + " where bletch_id = (select min(bletch_id) from " + table + ")) as first,"
        + "(select " + currency + ", " + TIME_COL + " from " + table
        + " where bletch_id = (select max(bletch_id) from " + table + ")) as last,"
        + "(select " + currency
        + ", " + TIME_COL + " from " + table + " where "
        + "(MOD(bletch_id," + getModNum(table) + ") = 0) "
        + "order by bletch_id) as b;";

    return query;
  }

  static String getModNum(String table) {
    String modNumQuery = "(select case "
    + " when count < " + TimeFrame.DAILY.getNumDataPoints()
    + " then " + TimeFrame.HOURLY.getTimeStep()

    + " when count < " + TimeFrame.WEEKLY.getNumDataPoints()
    + " then " + TimeFrame.DAILY.getTimeStep()

        + " when count < " + TimeFrame.MONTHLY.getNumDataPoints()
        + " then " + TimeFrame.MONTHLY.getTimeStep()

    + " else " + TimeFrame.QUARTERLY.getTimeStep()

    + " end "
    + " from (select max(bletch_id) as count from " + table+ ") as modnum) ";

    return modNumQuery;
  }
}
