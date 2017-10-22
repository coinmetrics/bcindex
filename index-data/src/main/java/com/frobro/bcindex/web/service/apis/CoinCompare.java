package com.frobro.bcindex.web.service.apis;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.domain.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by rise on 3/23/17.
 */
public class CoinCompare {
  private static final Logger LOG = LoggerFactory.getLogger(CoinCompare.class);
  private static final String DELIM = ",";
  private static final String COIN_COMPARE_BASE = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
  private static final String COIN_COMPARE_BTC_ENDPOINT = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC&tsyms=USD";
  private static final String CURRENCY = "&tsyms=USD";
  private final ObjectMapper mapper = new ObjectMapper();
  private final HttpApi http = new HttpApi();

  public Map<String,Index> getData(Set<String> coins) throws IOException {
    final String baseRequest = generateBaseRequest(coins);

    String request = COIN_COMPARE_BASE + removeTrailingDelim(baseRequest.toString()) + CURRENCY;
    String response = http.makeApiCall(request);

    return parseData(response, coins);
  }

  private String generateBaseRequest(Set<String> coins) {
    StringBuilder baseRequest = new StringBuilder();
    coins.stream().forEach(coin -> {
      baseRequest.append(coin).append(DELIM);
    });
    return baseRequest.toString();
  }

  private String removeTrailingDelim(String reqStr) {
    return reqStr.substring(0, reqStr.length() - DELIM.length());
  }

  public double getBtcPrice() throws IOException {
    return getBtcPrice(http.makeApiCall(COIN_COMPARE_BTC_ENDPOINT));
  }

  /************* Parsing logic *************/

  double getBtcPrice(String json) {
    try {

      JsonNode root = mapper.readTree(json);
      JsonNode node = root.get("BTC");
      return getPriceUsd(node);

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private double getPriceUsd(JsonNode node) {
    return node.get("USD").asDouble();
  }

  Map<String,Index> parseData(String response, Set<String> coins)
      throws IOException {

    JsonNode root = mapper.readTree(response);
    System.out.println(root);

    Map<String, Index> tickers = new TreeMap<>();

    for (String coin : coins) {
      if (root.has(coin)) {
        addCoin(tickers, root, coin);
      }
      else {
        LOG.error("no data for coin: " + coin);
      }
    }
    return tickers;
  }

  protected void addCoin(Map<String, Index> tickerMap, JsonNode node, String coin) {
    double price = getPriceUsd(node.get(coin));

    Index currPair = newIndex(coin, price);
    tickerMap.put(currPair.getName(), currPair);
  }

  private Index newIndex(String indexName, double price) {
    Index currPair = new Index().setName(indexName);
    return currPair.setLast(price);
  }
}

