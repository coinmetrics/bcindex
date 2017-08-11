package com.frobro.bcindex.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.BusinessRules;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by rise on 4/22/17.
 */
abstract public class BletchleyData {
  private static final BcLog log = BcLog.getLogger(BletchleyData.class);
  private static final String USD_BTC_KEY = "usd_btc";
  private static final double USD_BTC_MKT_CAP = 0;

  private BusinessRules businessRules = newBusinessRules();
  private Map<String,Index> lastIndexes = new HashMap<>();
  private long lastUpdateTime = 0;
  private double priceUsdBtc;

  public void setLastUpdate(long time) {
    this.lastUpdateTime = time;
  }

  public long getTimeStamp() {
    return lastUpdateTime;
  }

  public static String getUsdBtcTicker() {
    return USD_BTC_KEY;
  }

  abstract protected BusinessRules newBusinessRules();
  abstract Map<String,Index> populateTickers(String response,
                                             Set<String> indexes)
                                            throws IOException;

  public BletchleyData setLastUsdBtc(double last) {
    this.priceUsdBtc = last;
    return this;
  }

  public double getPriceUsdBtc() {
    return priceUsdBtc;
  }

  public BletchleyData setMembers(String apiResponse) {
    Set<String> indexes = businessRules.getIndexes();
    Map<String, Index> tickers = tickers(apiResponse, indexes);
    setMembers(tickers);
    return this;
  }

  protected Map<String, Index> tickers(String response, Set<String> indexes) {
    try {

      return populateTickers(response, indexes);

    } catch (IOException ioe) {
      log.error(ioe);
    }
    return new HashMap<>();
  }

  // public for testing
  public BletchleyData setMembers(Map<String, Index> members) {
    lastIndexes.putAll(members);
    return this;
  }

  protected JsonNode getRoot(String json) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(json);
  }

  protected void addTicker(Map<String,Index> tickerMap, String name, double price) {
    Index currPair = newIndex(name, price);
    tickerMap.put(currPair.getName(), currPair);
  }

  private Index newIndex(String indexName, double price) {
    Index currPair = new Index().setName(indexName);
    return currPair.setLast(price);
  }

  public Map<String,Index> getLastIndexes() {
    return lastIndexes;
  }
  @Override
  public String toString() {
    return "BletchleyData{" +
        "lastUpdateTime=" + lastUpdateTime +
        ", lastIndexes=" + lastIndexes +
        '}';
  }
}
