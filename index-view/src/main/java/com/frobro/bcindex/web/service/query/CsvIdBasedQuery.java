package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class CsvIdBasedQuery {
  private static final String LAST_PX_USD = "last_PxUsd";
  private static final String LAST_PX_BTC = "last_PxBtc";
  private static final String LAST_EPOCH_TIME = "last_epoch_time";
  private static final String FIRST_PX_USD = "first_PxUsd";
  private static final String FIRST_PX_BTC = "first_PxBtc";
  private static final String FRIST_EPOCH_TIME = "first_epoch_time";
  private static final String PX_USD = "px_usd";
  private static final String PX_BTC = "px_btc";
  private static final String EPOCH_TIME = "time_stamp";
  private static final String DELIM = ",";

  private final JdbcTemplate jdbc;

  public CsvIdBasedQuery(JdbcTemplate jdbc) {
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

  public List<String> getCsvContent(String tableName) {
    List<String> lines = new LinkedList<>();
    BletchCsv csv = new BletchCsv();

    String query = query(tableName);
    jdbc.query(query, (rs, rowNum) ->
            csv
                .epochTime(rs.getLong(EPOCH_TIME))
                .btcPrice(rs.getString(PX_BTC) + DELIM)
                .usdPrice(rs.getString(PX_USD))
                .newline()

                .firstEpochTime(rs.getLong(FRIST_EPOCH_TIME))
                .firstPxBt(rs.getString(FIRST_PX_BTC) + DELIM)
                .firstPxUsd(rs.getString(FIRST_PX_USD))

                .lastEpochTime(rs.getLong(LAST_EPOCH_TIME))
                .lastPxBtc(rs.getString(LAST_PX_BTC) + DELIM)
                .lastPxUsd(rs.getString(LAST_PX_USD))
    );

    // add header line
    csv.addHeader("time (CST), time (unix millis), index (BTC), index (USD)");

    return csv.getLines();
  }
}
