package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.model.BletchleyData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private static final String EMPTY_RESPONSE = "noop";
  private final static String COIN_CAP_ENDPOINT = "http://www.coincap.io/global";
  private final static String POLONIEX_ENDPOINT = "https://poloniex.com/public?command=returnTicker";
  private static final String COIN_CAP_ENDPOINT_20 = "http://www.coincap.io/front";

  private IndexCalculator indexCalculatorTen = new IndexCalculatorTen();
  private IndexCalculator indexCalculatorTwenty = new IndexCalculatorTwenty();
  // repos
  PrimeRepo repo;
  // db data
  private IndexDbDto lastIndex;
  private IndexDbDto lastEvenIndex;
  private IndexDbDto lastTwentyIdx;
  private IndexDbDto lastEvenTwentyIdx;
  // input data
  private BletchleyData lastDataTen = emptyData();
  private BletchleyData lastDataTwenty = emptyData();


  public void setIndexRepo(IndexRepo repo, EvenIdxRepo eRepo,
                           TwentyRepo tRepo, TwentyEvenRepo teRepo) {

    this.repo = PrimeRepo.getRepo(repo,eRepo,tRepo,teRepo);
  }

  public void saveIndices() {
    saveIndexTen();
    saveIndexTwenty();
  }

  private void saveIndexTen() {
    JpaIndexTen idx = new JpaIndexTen();
    populateJpa(idx, lastIndex);
    repo.saveTen(idx);

    JpaEvenIndex eIdx = new JpaEvenIndex();
    populateJpa(eIdx, lastEvenIndex);
    repo.saveTenEven(eIdx);
  }

  private void saveIndexTwenty() {
    JpaIdxTwenty idx = new JpaIdxTwenty();
    populateJpa(idx, lastTwentyIdx);
    repo.saveTwenty(idx);

    JpaTwentyEven evenIdx = new JpaTwentyEven();
    populateJpa(evenIdx, lastEvenTwentyIdx);
    repo.saveEvenTwenty(evenIdx);
  }

  private void populateJpa(JpaIndex idx, IndexDbDto dto) {
    idx.setIndexValueBtc(dto.indexValueBtc)
        .setIndexValueUsd(dto.indexValueUsd)
        .setTimeStamp(dto.timeStamp);
  }

  public TickerService updateTickers() {
    try {

      update();
      saveIndices();

    } catch (IOException ioe) {
      log.error("could not successfully update. ", ioe);
    }
    return this;
  }

  private void update() throws IOException {
    log.debug("updating latest data");

    // init new
    lastDataTen = new BletchleyData();
    lastDataTwenty = new BletchleyData();
    // get bit coin latest
    String btcResponse = makeApiCallBtc();
    updateTickerBtc(btcResponse);

    // update 10 idx
    String response = makeCallTenMembers();
    updateTenIdx(response);
    lastDataTen.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTen(lastDataTen);

    // update 20 idx
    String response20 = makeApiCallTwenty();
    updateTwentyIdx(response20);
    lastDataTwenty.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTwenty(lastDataTwenty);
  }

  private void calculateAndSetIndexesTwenty(BletchleyData data) {
    indexCalculatorTwenty.updateLast(data);
    lastTwentyIdx = indexCalculatorTwenty.calcuateOddIndex();
    lastEvenTwentyIdx = indexCalculatorTwenty.calculateEvenIndex();
  }

  private void calculateAndSetIndexesTen(BletchleyData data) {
    indexCalculatorTen.updateLast(data);
    lastIndex = indexCalculatorTen.calcuateOddIndex();
    lastEvenIndex = indexCalculatorTen.calculateEvenIndex();
  }

  private String makeApiCallTwenty() throws IOException {
    return makeApiCall(COIN_CAP_ENDPOINT_20);
  }

  public void updateTickerBtc(String response) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    CoinCapDto dto = mapper.readValue(response, CoinCapDto.class);
    double btcPrice = dto.getBtcPrice();
    lastDataTen.setLastUsdBtc(btcPrice);
    lastDataTwenty.setLastUsdBtc(btcPrice);
  }

  private void updateTwentyIdx(String apiResponse) throws IOException {
    Set<String> indexes = new BusRulesTwenty().getIndexes();
    Map<String, Index> tickers = populate20(apiResponse, indexes);
    lastDataTwenty.setMembers(tickers);
  }

  private Map<String,Index> populate20(String json,
                                       Set<String> indexes) throws IOException {
    Map<String, Index> tickers = new TreeMap<>();
    ArrayNode root = (ArrayNode) getRoot(json);

    root.elements().forEachRemaining(node -> {
      JsonNode pxNd = node.get(CurrPairJson.PRICE_KEY_20);
      double price = pxNd.asDouble();

      JsonNode symbolNd = node.get(CurrPairJson.NAME_KEY_20);
      String symbol = symbolNd.asText();

      if (indexes.contains(symbol)) {
        addTicker(tickers, symbol, price);
      }
    });
    return tickers;
  }

  private void updateTenIdx(String apiResponse) throws IOException {
    Set<String> indexes = new BusRulesTen().getIndexes();
    Map<String, Index> tickers = populateTickers(apiResponse, indexes);
    lastDataTen.setMembers(tickers);
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

  private Map<String,Index> populateTickers(String json, Set<String> indexes) throws IOException {
    Map<String, Index> tickers = new TreeMap<>();
    JsonNode root = getRoot(json);

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
    addTicker(tickerMap, name, getVal(node));
  }

  private void addTicker(Map<String,Index> tickerMap, String name, double price) {
    Index currPair = newIndex(name, price);
    tickerMap.put(currPair.getName(), currPair);
  }

    private Index newIndex(String indexName, double price) {
    Index currPair = new Index().setName(indexName);
    return currPair.setLast(price);
  }

  private double getVal(JsonNode node) {
    return node.get(CurrPairJson.LAST_KEY).asDouble();
  }

  public Collection<Index> getLatestCap() {
    return indexCalculatorTen.getSortedValues();
  }

  void init() {
    indexCalculatorTen.updateLast(emptyData());
  }

  private BletchleyData emptyData() {
    return new BletchleyData();
  }

  TickerService put(String name, double cap) {
    indexCalculatorTen.put(name, 0, cap);
    return this;
  }
}

