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

  public void updateTickers() throws IOException {

    CloseableHttpClient httpClient = HttpClients.createDefault();
    String response = EMPTY_RESPONSE;
    try {
      HttpGet getRequest = new HttpGet(
          endPoint);
      getRequest.addHeader("accept", "application/json");
      response = httpClient.execute(getRequest, createResponseHandler());

    } finally {
    httpClient.close();
    }
    populateTickers(response);
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
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(json);
    root.fields().forEachRemaining(node -> {
      Index currPair = new Index().setName(node.getKey());

      currPair.setLast(Double.parseDouble(node.getValue()
          .get(CurrPairJson.LAST_KEY).textValue()));

      tickers.put(currPair.getName(), currPair);
    });
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

  public double getLatestSum() {
    double sum = 0;
    for (Index ticker : tickers.values()) {
      if (ticker.isMktCapValid()) {
        sum += ticker.getMktCap();
      }
    }
    return sum;
  }

  public TickerService put(String name, double cap) {
    Index idx = new Index().setName(name)
        .setLast(0).setMktCap(cap);
    tickers.put(idx.getName(), idx);
    return this;
  }
}

