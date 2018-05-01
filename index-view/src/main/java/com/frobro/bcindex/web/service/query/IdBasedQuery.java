package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class IdBasedQuery {
  protected static final String PX_USD = "px_usd";
  protected static final String PX_BTC = "px_btc";
  protected static final String EPOCH_TIME = "time_stamp";
  protected static final String DELIM = ",";

  protected final JdbcTemplate jdbc;

  protected IdBasedQuery(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public String query(String table) {
    // TODO: hard coded Postgres functions below (to_timestamp, date_part)
    return "select index_value_usd as " + PX_USD +
        ", index_value_btc as " + PX_BTC +
        ", time_stamp as " + EPOCH_TIME +
        "   from " + table + " " +
        "   where date_part('hour',to_timestamp(time_stamp/1000)) = 0 " +
        "   and date_part('minute',to_timestamp(time_stamp/1000)) = 0 order by bletch_id desc";
  }
}
