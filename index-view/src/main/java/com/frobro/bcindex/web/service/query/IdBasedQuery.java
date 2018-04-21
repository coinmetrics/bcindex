package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class IdBasedQuery {
  protected static final String LAST_PX_USD = "last_PxUsd";
  protected static final String LAST_PX_BTC = "last_PxBtc";
  protected static final String LAST_EPOCH_TIME = "last_epoch_time";
  protected static final String FIRST_PX_USD = "first_PxUsd";
  protected static final String FIRST_PX_BTC = "first_PxBtc";
  protected static final String FRIST_EPOCH_TIME = "first_epoch_time";
  protected static final String PX_USD = "px_usd";
  protected static final String PX_BTC = "px_btc";
  protected static final String EPOCH_TIME = "time_stamp";
  protected static final String DELIM = ",";

  protected final JdbcTemplate jdbc;

  protected IdBasedQuery(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public String query(String table) {
    return "select last.index_value_usd as " + LAST_PX_USD + ", last.index_value_btc as " + LAST_PX_BTC
        + ", last.time_stamp as " + LAST_EPOCH_TIME + ", " +
        "first.index_value_usd as " + FIRST_PX_USD + ", first.index_value_btc as " + FIRST_PX_BTC
        +  ", first.time_stamp as " + FRIST_EPOCH_TIME + ", " +
        "b.index_value_usd as " + PX_USD + ", b.index_value_btc as " + PX_BTC
        + ", b.time_stamp as " + EPOCH_TIME +
        " from " +
        "(select index_value_usd, index_value_btc, time_stamp from " + table + " where bletch_id = (select min(bletch_id) from " + table + ")) as first, " +
        "(select index_value_usd, index_value_btc, time_stamp from " + table + " where bletch_id = (select max(bletch_id) from " + table + ")) as last, " +
        "(select index_value_usd, index_value_btc, time_stamp from " + table + " where (MOD(bletch_id,"+ MaxTimeQuery.getModNum(table) + ") = 0) order by bletch_id) as b;";
  }
}
