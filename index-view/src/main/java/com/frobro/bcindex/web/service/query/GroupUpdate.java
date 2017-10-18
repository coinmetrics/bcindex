package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.IndexType;

import java.util.HashMap;
import java.util.Map;

public class GroupUpdate {
  private final Map<IndexType,IndexUpdate> updates = new HashMap<>();

  public IndexUpdate get(IndexType reqIdx) {
    return updates.get(reqIdx);
  }

  public GroupUpdate updateAll(double usd, double btc, long time) {
    updateTen(usd, btc, time);
    updateTenEven(usd, btc, time);
    updateTwenty(usd, btc, time);
    updateTwentyEven(usd, btc, time);
    updateEther(usd, btc, time);
    updateEtherEven(usd, btc, time);
    return this;
  }

  public GroupUpdate updateTen(double usd,
                               double btc,
                               long time) {

    updates.put(IndexType.ODD_INDEX, newUpdate(usd, btc, time));
    return this;
  }

  private IndexUpdate newUpdate(double usd, double btc,
                                long time) {
    return new IndexUpdate()
        .setUsdPrice(usd)
        .setBtcPrice(btc)
        .setTimeStamp(time);
  }

  public GroupUpdate updateTenEven(double usd,
                                   double btc,
                                   long time) {

    updates.put(IndexType.EVEN_INDEX, newUpdate(usd, btc, time));
    return this;
  }

  public GroupUpdate updateTwenty(double usd,
                               double btc,
                               long time) {

    updates.put(IndexType.INDEX_TWENTY, newUpdate(usd, btc, time));
    return this;
  }

  public GroupUpdate updateTwentyEven(double usd,
                                   double btc,
                                   long time) {

    updates.put(IndexType.EVEN_TWENTY, newUpdate(usd, btc, time));
    return this;
  }

  public GroupUpdate updateEther(double usd,
                                   double btc,
                                   long time) {
    updates.put(IndexType.INDEX_ETH, newUpdate(usd, btc, time));
    return this;
  }

  public GroupUpdate updateEtherEven(double usd,
                                   double btc,
                                   long time) {

    updates.put(IndexType.EVEN_ETH, newUpdate(usd, btc, time));
    return this;
  }
}