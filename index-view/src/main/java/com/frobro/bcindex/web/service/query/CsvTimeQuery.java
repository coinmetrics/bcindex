package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.TimeFrame;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CsvTimeQuery {
  private static final String MIN_TIME = "minTime";
  private static final String MAX_TIME = "maxTime";

  private static final String alias = "alias";
  private static final String asAlias = ") as " + alias;
  private static final String usdAlias = "usd";
  private static final String btcAlias = "btc";
  private static final String timeAlias = "time";
  private static final String delim = "_";

  private final JdbcTemplate jdbc;
  private final long timeIntervalMillis;
  private Integer numDataPts = 0;

  public static String getTimeCol(int iteration) {
    return getCol(iteration, timeAlias);
  }

  public static String getUsdCol(int iteration) {
    return getCol(iteration, usdAlias);
  }

  public static String getBtcCol(int iteration) {
    return getCol(iteration, btcAlias);
  }

  private static String getCol(int iteration, String valAlias) {
    return alias + iteration + delim + valAlias;
  }

  /*
   * constructor
   */
  public CsvTimeQuery(JdbcTemplate jdbc, long intervalMillis) {
    this.jdbc = jdbc;
    this.timeIntervalMillis = intervalMillis;
  }

  public void getCacheContent(String table, TimeFrame timeFrame,
                              ApiResponse usd, ApiResponse btc) {

    CachePopulator populator = new CachePopulator(usd, btc);

    String minMaxQuery = getMaxAndMinQuery(table, timeFrame.getTimeSpan());
    MinAndMax minAndMax = getMinAndMaxTime(minMaxQuery);

    String query = generateQuery(minAndMax.min, minAndMax.max, table);

    // populate should populate the two responses
    execute(query, numDataPts, populator);
  }

  public List<String> getCsvContent(String tableName) {
    BletchCsv dto = new BletchCsv();
    // get earliest min(time)
    MinAndMax minAndMax = getMinAndMaxTime(getMaxAndMinQuery(tableName));

    // generate query based on min(time) and timeframe
    String query = generateQuery(minAndMax.min, minAndMax.max, tableName);

    // populate POJO from execute results
    execute(query, numDataPts, dto);

    dto.prependHeader("Time (CST), Time Epoch Millis, Price BTC, Price USD");
    return dto.getLines();
  }

  private MinAndMax getMinAndMaxTime(String query) {

    MinAndMax minMax = new MinAndMax();
    jdbc.query(query, (rs, rowNum) ->
            minMax.setMin(rs.getLong(MIN_TIME))
                .setMax(rs.getLong(MAX_TIME))
    );

    return minMax;
  }

  private String getMaxAndMinQuery(String table, long intervalMillis) {
    return "select min(b.time_stamp) as min, max(b.time_stamp) as max from  " +
        "(select max(time_stamp) as max from " + table + ") as a, " +
        "(select time_stamp from odd_index) as b where time_stamp > (a.max - "
        + intervalMillis + ");";
  }

  // protected so it can be overriden by children
  protected String getMaxAndMinQuery(String table) {
    return "select min(time_stamp) as minTime, max(time_stamp) as maxTime "
        + " from " + table + ";";
  }

  private String generateQuery(long minTime, long maxTime, String tableName) {
    StringBuilder query = new StringBuilder();

    // build this up as new cols are added
    StringBuilder rootSelect = new StringBuilder("select ");

    String selectQuery = "(select time_stamp as " + timeAlias
        + ", index_value_usd as " + usdAlias
        + ", index_value_btc as " + btcAlias + " ";

    // append alias
    String tableAndWhere = " from " + tableName + " where time_stamp ";
    // append condition
    String order = " order by time_stamp desc limit 1 "; // append table alias

    numDataPts = 1;

    query.append(selectQuery).append(tableAndWhere)
        .append(" = ").append(minTime).append(" limit 1 ")
        .append(asAlias).append(numDataPts).append(",");

    // update root
    appendRoot(rootSelect, numDataPts, ", ");

    for (long time=minTime + timeIntervalMillis; time < maxTime; time+= timeIntervalMillis) {
      numDataPts++;
      query.append(selectQuery).append(tableAndWhere)
          .append(" <= ").append(time).append(order).append(asAlias)
          .append(numDataPts).append(",");

      // update root
      appendRoot(rootSelect, numDataPts, ", ");
    }

    numDataPts++;
    query.append(selectQuery).append(tableAndWhere)
        .append(" = ").append(maxTime).append(" limit 1 ")
        .append(asAlias).append(numDataPts).append(";");

    // update root
    appendRoot(rootSelect, numDataPts, " ");
    rootSelect.append(" from ");

    // repend root select
    query.insert(0, rootSelect.toString());

    return query.toString();
  }

  private void appendRoot(StringBuilder b, int j, String endChar) {
    b
        // append time
        .append(alias).append(j).append(".")
        .append(timeAlias).append(" as ")
        .append(alias).append(j).append(delim).append(timeAlias)
        .append(", ")
        // append usd
        .append(alias).append(j).append(".")
        .append(usdAlias).append(" as ")
        .append(alias).append(j).append(delim).append(usdAlias)
        .append(", ")
        // append btc
        .append(alias).append(j).append(".")
        .append(btcAlias).append(" as ")
        .append(alias).append(j).append(delim).append(btcAlias)
        .append(endChar);
  }

  private Populatable execute(String query,
                            final int size,
                            Populatable dto) {

    jdbc.query(query, (rs, rowNum) ->
            dto.populate(rs, size)
    );
    return dto;
  }

  private class MinAndMax {
    long min;
    long max;

    MinAndMax setMin(long min) {
      this.min = min;
      return this;
    }

    MinAndMax setMax(long max) {
      this.max = max;
      return this;
    }
  }
}
