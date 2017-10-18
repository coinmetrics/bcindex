package com.frobro.bcindex.web.service;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.LastPriceCache;
import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.service.query.QueryLatest;
import com.frobro.bcindex.web.service.time.TimeService;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 3/23/17.
 */
public class DbTickerService implements DataProvider {

  private static final BcLog log = BcLog.getLogger(DbTickerService.class);
  private final LastPriceCache priceCache = new LastPriceCache();
  private TimeSeriesService timeSeriesService = new TimeSeriesService();
  private JdbcTemplate jdbc;

  public static String toJson(Object response) {
    String responseStr = "error";
    try {

      responseStr = new ObjectMapper().writeValueAsString(response);

    } catch (JsonProcessingException jpe) {
      log.error(jpe);
    }
    return responseStr;
  }

  public DbTickerService setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
    timeSeriesService.setJdbc(jdbc);
    return this;
  }

  public int getNumDataPointsOnGraph() {
    return timeSeriesService.getNumPoints();
  }

  public String respondAsJson(RequestDto req) {
    return toJson(getData(req));
  }

  @Override
  public ApiResponse getData(RequestDto req) {
    return timeSeriesService.getData(req);
  }

  public DbTickerService updateTickers() {
    try {

      update();

    } catch (IOException ioe) {
      log.error("could not successfully update. ", ioe);
    }
    return this;
  }

  private void update() throws IOException {
    if (priceCache.isMinuteSinceLastUpdate()) {
      log.info("getting latest data");
      getDataFromDb();
    }
  }

  private void getDataFromDb() {
  // replace with mult table query to populate last prices
    String query = QueryLatest.QUERY;

    jdbc.query(query, (rs, rowNum) -> priceCache
            .setTenPxBtc(rs.getDouble(QueryLatest.TEN_IDX_BTC))
            .setTenPxUsd(rs.getDouble(QueryLatest.TEN_IDX_USD))
            .setTenEvenPxBtc(rs.getDouble(QueryLatest.TEN_IDX_EVEN_BTC))
            .setTenEvenPxUsd(rs.getDouble(QueryLatest.TEN_IDX_EVEN_USD))
            .setTwentyPxBtc(rs.getDouble(QueryLatest.TWENTY_IDX_BTC))
            .setTwentyPxUsd(rs.getDouble(QueryLatest.TWENTY_IDX_USD))
            .setTwentyEvenPxBtc(rs.getDouble(QueryLatest.TWENTY_IDX_EVEN_BTC))
            .setTwentyEvenPxUsd(rs.getDouble(QueryLatest.TWENTY_IDX_EVEN_USD))
    );
    priceCache.setTimeStamp(TimeService.currentTimeMillis());
  }

  public LastPriceCache getCache() {
    return priceCache;
  }
}

