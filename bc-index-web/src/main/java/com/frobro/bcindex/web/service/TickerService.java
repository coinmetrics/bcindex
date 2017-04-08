package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

  private Map<String, Index> tickers = new TreeMap<>();
  private String endPoint = "https://poloniex.com/public?command=returnTicker";
  private final double divisor = new BusinessRules().getDivisor();
  private double numUsdperBtc = -1;

  public void updateTickers() throws IOException {
    String response = makeApiCall();
    populateTickers(response);
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

  private void populateTickers(String json) throws IOException {
    JsonNode root = getRoot(json);

    Set<String> indexes = Index.getIndexes();

    root.fields().forEachRemaining(node -> {

      String indexName = node.getKey();
      if (indexes.contains(indexName)) {
        saveIndex(indexName, getVal(node.getValue()));
        indexes.remove(indexName);
      }
      else if (Index.isBitCoinTicker(indexName)) {
        numUsdperBtc = Double.parseDouble(getVal(node.getValue()));
      }
    });
  }

  private JsonNode getRoot(String json) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(json);
  }

  private void saveIndex(String indexName, String priceStr) {
    Index currPair = newIndex(indexName, priceStr);
    tickers.put(currPair.getName(), currPair);
  }

  private Index newIndex(String indexName, String priceStr) {
    Index currPair = new Index().setName(indexName);
    return currPair.setLast(Double.parseDouble(priceStr));
  }

  private String getVal(JsonNode node) {
    return node.get(CurrPairJson.LAST_KEY).textValue();
  }

  public Collection<Index> getLatestCap() {
    List<Index> sorted = new ArrayList<>(tickers.values());
    Collections.reverse(sorted);
    return sorted;
  }

  public List<String> mktCapAsStrings() {
    List<String> idxDisplay = new ArrayList<>(tickers.size());
    tickers.values().stream().forEach(val -> {
      double mktCap = val.getMktCap();

      idxDisplay.add(val.getName()
          + " [mkt cap: " + mktCap
          + ", last price: " + val.getLast() + "]");
    });
    return idxDisplay;
  }

  public double getIndexValue() {
    double sum = 0;
    for (Index ticker : tickers.values()) {
      if (ticker.isMktCapValid()) {
        sum += ticker.getMktCap();
      }
    }
    return ((sum + getConstant())/divisor)*numUsdperBtc;
  }

  public double getConstant() {
    return 16223125;
  }

  public TickerService put(String name, double cap) {
    Index idx = new Index().setName(name)
        .setLast(0).setMktCap(cap);
    tickers.put(idx.getName(), idx);
    return this;
  }
}

