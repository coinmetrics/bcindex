package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedList;
import java.util.List;

public class CsvIdBasedQuery extends IdBasedQuery {

  public CsvIdBasedQuery(JdbcTemplate jdbc) {
    super(jdbc);
  }

  public List<String> getDbData(String tableName) {
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
