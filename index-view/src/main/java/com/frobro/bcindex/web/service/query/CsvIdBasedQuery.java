package com.frobro.bcindex.web.service.query;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class CsvIdBasedQuery extends IdBasedQuery {

  public CsvIdBasedQuery(JdbcTemplate jdbc) {
    super(jdbc);
  }

  public List<String> getDbData(String tableName) {
    BletchCsv csv = new BletchCsv();

    String query = query(tableName);
    jdbc.query(query, (rs, rowNum) ->
            csv
                .epochTime(rs.getLong(EPOCH_TIME))
                .btcPrice(rs.getString(PX_BTC) + DELIM)
                .usdPrice(rs.getString(PX_USD))
                .newline()
    );

    // add header line
    csv.addHeader("time (UTC), time (unix millis), index (BTC), index (USD)");

    return csv.getLines();
  }
}
