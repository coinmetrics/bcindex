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
  private static final String EMPTY_RESPONSE = "noop";
  private final static String COIN_CAP_ENDPOINT = "http://www.coincap.io/global";
  private final static String POLONIEX_ENDPOINT = "https://poloniex.com/public?command=returnTicker";

  private IndexCalculator indexCalculator = new IndexCalculator();
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

  private void calculateAndSetIndexes(BletchleyData data) {
    indexCalculator.updateLast(data);
    lastIndex = indexCalculator.calcuateOddIndex();
    lastEvenIndex = indexCalculator.calculateEvenIndex();
  }

  public void updateTickerBtc(String response) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    CoinCapDto dto = mapper.readValue(response, CoinCapDto.class);
    lastData.setLastUsdBtc(dto.getBtcPrice());
  }

  private void update(String apiResponse)throws IOException {
    Map<String, Index> tickers = populateTickers(apiResponse);
    lastData.setMembers(tickers);
  }

  public String makeApiCallBtc() throws IOException {
    return makeApiCall(COIN_CAP_ENDPOINT);
  }
  
  private String makeCallTenMembers() throws IOException {
    return makeApiCall(POLONIEX_ENDPOINT);
  }

  private String makeApiCall(String endpoint) throws IOException {
    String response = EMPTY_RESPONSE;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {

      HttpGet getRequest = new HttpGet(
          endpoint);
      getRequest.addHeader("accept", "application/json");
      response = httpClient.execute(getRequest, createResponseHandler());

    } finally {
      httpClient.close();
    }
    return response;
  }
  private ResponseHandler<String> createResponseHandler() {
    return new ResponseHandler<String>() {

      @Override
      public String handleResponse(
          final HttpResponse response) throws ClientProtocolException, IOException {

        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = response.getEntity();
          return entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException("Unexpected response status: " + status);
        }
      }
    };
  }

  private Map<String,Index> populateTickers(String json) throws IOException {
    Map<String, Index> tickers = new TreeMap<>();
    JsonNode root = getRoot(json);

    Set<String> indexes = new BusinessRules().getIndexes();

    root.fields().forEachRemaining(node -> {

      String indexName = node.getKey();
      if (indexes.contains(indexName)) {
        addTicker(tickers, indexName, node.getValue());
      }
    });
    return tickers;
  }

  private JsonNode getRoot(String json) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(json);
  }

  private void addTicker(Map<String,Index> tickerMap, String name, JsonNode node) {
    Index currPair = newIndex(name, getVal(node));
    tickerMap.put(currPair.getName(), currPair);
  }

  private Index newIndex(String indexName, String priceStr) {
    Index currPair = new Index().setName(indexName);
    return currPair.setLast(Double.parseDouble(priceStr));
  }

  private String getVal(JsonNode node) {
    return node.get(CurrPairJson.LAST_KEY).textValue();
  }

  public Collection<Index> getLatestCap() {
    return indexCalculator.getSortedValues();
  }

  public double getIndexValue() {
    return lastIndex.getIndexValueUsd();
  }

  public double getEvenIndexValue() {
    return lastEvenIndex.getIndexValueUsd();
  }

  public double getConstant() {
    return indexCalculator.getConstant();
  }

  void init() {
    indexCalculator.updateLast(emptyData());
  }

  private BletchleyData emptyData() {
    return new BletchleyData();
  }

  DbTickerService put(String name, double cap) {
    indexCalculator.put(name, 0, cap);
    return this;
  }
}
