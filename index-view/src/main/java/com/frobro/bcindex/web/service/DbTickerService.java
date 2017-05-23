package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.service.persistence.EvenIdxRepo;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 3/23/17.
 */
public class DbTickerService {

  private static final BcLog log = BcLog.getLogger(DbTickerService.class);

  private BletchleyData lastData = emptyData();
  private IndexRepo oddRepo;
  private EvenIdxRepo evenRepo;
  private JpaIndex lastIndex;
  private JpaEvenIndex lastEvenIndex;

  private TimeSeriesService timeSeriesService = new TimeSeriesService();


  public DbTickerService() {
  }

  public DbTickerService setJdbc(JdbcTemplate jdbc) {
    timeSeriesService.setJdbc(jdbc);
    return this;
  }

  public DbTickerService setOddRepo(IndexRepo repo) {
    this.oddRepo = repo;
    return this;
  }

  public DbTickerService setEvenRepo(EvenIdxRepo repo) {
    this.evenRepo = repo;
    return this;
  }

  public int getNumDataPointsOnGraph() {
    return timeSeriesService.getNumPoints();
  }

  public String respondAsJson(RequestDto req) {
    return toJson(timeSeriesService.getData(req));
  }

  public ApiResponse respond(RequestDto req) {
    return timeSeriesService.getData(req);
  }

  private String toJson(ApiResponse response) {
    String responseStr = "error";
    try {

      responseStr = new ObjectMapper().writeValueAsString(response);

    } catch (JsonProcessingException jpe) {
      log.error(jpe);
    }
    return responseStr;
  }

  public DbTickerService updateTickers() {
    try {

      Update();

    } catch (IOException ioe) {
      log.error("could not successfully update. ", ioe);
    }
    return this;
  }

  private void Update() throws IOException {
    log.info("getting latest data");
    getDataFromDb();
  }

  private void getDataFromDb() {
    List<JpaIndex> result = oddRepo
        .findFirst1ByOrderByTimeStampDesc();
    if (result.size() > 0) {
      lastIndex = result.get(0);
    }
    else {
      log.error("tried to get the index value "
        + "from the db but there is no data");
    }

    List<JpaEvenIndex> evenList = evenRepo
        .findFirst1ByOrderByTimeStampDesc();
    if (evenList.size() > 0) {
      lastEvenIndex = evenList.get(0);
    }
    else {
      log.error("tried to get the EVEN index value "
          + "from the db but there is no data");
    }
  }

  public double getIndexValue() {
    return lastIndex.getIndexValueUsd();
  }

  public double getEvenIndexValue() {
    return lastEvenIndex.getIndexValueUsd();
  }

  private BletchleyData emptyData() {
    return new BletchleyData();
  }
}

