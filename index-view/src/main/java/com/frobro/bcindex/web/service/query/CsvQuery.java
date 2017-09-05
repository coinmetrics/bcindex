package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedList;
import java.util.List;

public class CsvQuery {

  private static final String TIME_COL = "time_cst";
  private static final String USD_COL = "value_usd";
  private static final String BTC_COL = "value_btc";
  private static final String EPOCH_COL = "time_epoch";
  private static final String DELIM = ",";

  private static final String QUERY_FIRST = "select b.time as " +TIME_COL
      + ", b.index_value_usd as " + USD_COL + ", b.index_value_btc as " + BTC_COL
      + ", b.time_stamp as " + EPOCH_COL
      + " from (select a.index_value_usd, a.index_value_btc, a.time_stamp, a.time as time, "
      + " rank() over (partition by a.day order by a.time) from (select index_value_usd, "
      + "index_value_btc, time_stamp, to_timestamp(time_stamp/1000) as time, date_part('day', "
      + " to_timestamp(time_stamp/1000)) as day from ";

  private static final String QUERY_LAST = " )"
      + " as a) as b where b.rank = 1 order by b.time_stamp;";

  private final JdbcTemplate jdbc;

  public CsvQuery(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<String> getCsvContent(String tableName) {
    List<String> lines = new LinkedList<>();

    // add header line
    lines.add("time (CST), time (unix millis), index (BTC), index (USD)");

    jdbc.query(query(tableName), (rs, rowNum) ->
      lines.add(rs.getString(TIME_COL) + DELIM
          + rs.getString(EPOCH_COL) + DELIM
          + rs.getString(BTC_COL) + DELIM
          + rs.getString(USD_COL)
      ));
    return lines;
  }

  private String query(String tableName) {
    return QUERY_FIRST + tableName + QUERY_LAST;
  }
}
