package com.frobro.bcindex.web.service.apis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.exceptions.ApiParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class NomicsApi  {
  private static final Logger LOG = LoggerFactory.getLogger(NomicsApi.class);

  private static final String BASE_URL = "https://api.nomics.com/v1/prices";
  private static final String API_KEY = "8ab88c64570680aeb728a3109e69dd96";
  private static final String DELIM = "?key=";
  private static final String NOMICS_END_PT = BASE_URL + DELIM + API_KEY;
  private static final String SYMBOL_KEY = "currency";
  private static final String PRICE_KEY = "price";
  private static final String BITCOIN_SYMBOL = "BTC";

  private final ObjectMapper mapper = new ObjectMapper();
  private HttpApi http = new HttpApi();
  private Set<String> batchCoins = new HashSet<>();

  public NomicsApi addBtcToBatch() {
    Set<String> coinSet = new HashSet<>();
    coinSet.add(BITCOIN_SYMBOL);
    batchCoins(coinSet);
    return this;
  }

  public NomicsApi batchCoins(Set<String> coins) {
    batchCoins.addAll(coins);
    return this;
  }

  public double extractBtcFromData(Map<String,Index> data) {
    Index btc = data.get(BITCOIN_SYMBOL);

    if (btc == null) {
      throw new IllegalStateException("No bitcoin price received from api");
    }

    return btc.getLast();
  }

  public Map<String,Index> callBatchedData() throws IOException {
    Map<String,Index> data;
    data = getData(batchCoins);

    batchCoins.clear();
    return data;
  }

  public Map<String,Index> getData(Set<String> coins) throws IOException {
    String response = http.makeApiCall(NOMICS_END_PT);

    return parseData(response, coins);
  }

  /************* Parsing logic *************/

  private Map<String,Index> parseData(String response, Set<String> bletchCoins)
      throws IOException {


    Map<String, Index> tickers = new TreeMap<>();
    JsonNode root = mapper.readTree(response);

    int foundCoinsCnt = 0;

    for (String coin : bletchCoins) {
      Iterator<JsonNode> itr = root.iterator();

      int initialSize = foundCoinsCnt;

      // for this coin iterate through all the in the response
      while(itr.hasNext()) {
        JsonNode node = itr.next();

        JsonNode coinNode = node.get(SYMBOL_KEY);
        JsonNode priceNode = node.get(PRICE_KEY);

        // if the key doesn't exist
        if (coinNode == null) {
          throwNullNodeError(SYMBOL_KEY, node);
        }
        if (priceNode == null) {
          throwNullNodeError(PRICE_KEY, node);
        }

        // if this coin is in this node
        if (coin.equalsIgnoreCase(coinNode.asText())) {
          addCoin(tickers, coin, priceNode.asDouble());
          foundCoinsCnt++;

          // stop searching we found it
          break;
        }
      } // end while loop

      // if the coin was found the size should increase by one
      // otherwise we didn't find it
      if ((foundCoinsCnt-initialSize) != 1) {
        LOG.error("no data for coin: " + coin);
      }
    } // end for loop

    return tickers;
  }

  private void throwNullNodeError(String key, JsonNode node) {
    throw new ApiParsingException("could not find expected field: " + key
        + " in node: " + node);
  }

  private void addCoin(Map<String, Index> tickerMap, String coin, double value) {
    Index index = new Index()
        .setName(coin)
        .setLast(value);
    tickerMap.put(coin, index);
  }

  /***** test methods ****/
  protected void mock(HttpApi api) {
    this.http = api;
  }
}
