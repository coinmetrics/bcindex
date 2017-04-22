package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.domain.Index;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rise on 4/22/17.
 */
public class BletchleyData {
  private static final String USD_BTC_KEY = "usd_btc";
  private static final double USD_BTC_MKT_CAP = 0;

  private double lastUsdBtc;
  private Map<String,Index> lastIndexes = new HashMap<>();

  public BletchleyData setLastUsdBtc(double last) {
    Index newEntry = new Index()
        .setName(USD_BTC_KEY)
        .setMktCap(USD_BTC_MKT_CAP)
        .setLast(last);
    lastIndexes.put(USD_BTC_KEY, newEntry);
    return this;
  }

  public BletchleyData setMembers(Map<String, Index> members) {
    lastIndexes.putAll(members);
    return this;
  }

  public Map<String,Index> getLastIndexes() {
    return lastIndexes;
  }
}
