package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
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

  private String endPoint = "https://poloniex.com/public?command=returnTicker";
  private IndexCalculator indexCalculator = new IndexCalculator();

  public TickerService() {
  }

  public void updateTickers() throws IOException {
    String response = makeApiCall();
    updateTickers(response);
  }

  public void updateTickers(String apiResponse)throws IOException {
    Map<String, Index> tickers = populateTickers(apiResponse);
    indexCalculator.updateLast(tickers);
  }

  private String makeApiCall() throws IOException {
    String response = EMPTY_RESPONSE;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {

      HttpGet getRequest = new HttpGet(
          endPoint);
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
    return indexCalculator.getLastIndexValue();
  }

  public double getConstant() {
    return indexCalculator.getConstant();
  }

  void init() {
    indexCalculator.updateLast(new HashMap<>());
  }

  TickerService put(String name, double cap) {
    indexCalculator.put(name, 0, cap);
    return this;
  }
}

