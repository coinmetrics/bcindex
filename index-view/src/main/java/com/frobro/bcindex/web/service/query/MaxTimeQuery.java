package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 6/15/17.
 */
public class MaxTimeQuery extends TimeSeriesQuery {
  public MaxTimeQuery(RequestDto req) {
    super(req);
  }

  @Override
  public ApiResponse execute(JdbcTemplate jdbc, ApiResponse response) {
    // ADD TOTAL CNT COLUMN
    jdbc.query(queryString(), (rs, rowNum) ->
            response.addPrice((rs.getDouble(getCurrency())))
                .addTime(rs.getString(TimeSeriesQuery.TIME_COL))
                .updateLast(rs.getDouble(TimeSeriesQuery.LAST_PX_COL))
    );
    return response;
  }

  @Override
  public String queryString() {
    String query = "select " + COUNT_COL + ", last." + currency + " as "
        + LAST_PX_COL + ", b." + currency
        + ", b." + TIME_COL
        + " from "
        + "select count(*) from " + table + " as " + COUNT_COL + ", "
        + "(select " + currency + " from " + table
        + " where id = (select count(*) from " + table + ")) as last,"
        + "(select " + currency
        + ", " + TIME_COL + " from " + table + " where "
        + "(MOD(id," + getModNum() + ") = 0) "
        + "order by id) as b;";

    return query;
  }

  private String getModNum() {
    return "(select count(*) from "
        + table + ") / 60";
  }
}
