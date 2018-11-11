package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.IndexType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupUpdate {
  private final Map<IndexType,IndexUpdate> updates = new HashMap<>();

  public IndexUpdate get(IndexType reqIdx) {
    return updates.get(reqIdx);
  }

  public Set<IndexType> getIndices() {
    return new HashSet<>(updates.keySet());
  }

  public long getTime(IndexType idx) {
    return updates.get(idx).getTimeStamp();
  }

  public GroupUpdate updateAll(double usd, double btc,
                               long time, long maxBletchId) {
    updateTen(usd, btc, time, maxBletchId);
    updateTenEven(usd, btc, time, maxBletchId);
    updateTwenty(usd, btc, time, maxBletchId);
    updateTwentyEven(usd, btc, time, maxBletchId);
    updateEther(usd, btc, time, maxBletchId);
    updateEtherEven(usd, btc, time, maxBletchId);
    return this;
  }

  public GroupUpdate update(IndexType index,
                            double usd,
                            double btc,
                            long time,
                            long bletchId) {

    updates.put(index, newUpdate(usd, btc, time, bletchId));
    return this;
  }

  public GroupUpdate updateTen(double usd,
                               double btc,
                               long time,
                               long bletchId) {

    update(IndexType.ODD_INDEX, usd, btc, time, bletchId);
    return this;
  }

  private IndexUpdate newUpdate(double usd, double btc,
                                long time,
                                long bletchId) {
    return new IndexUpdate()
        .setUsdPrice(usd)
        .setBtcPrice(btc)
        .setTimeStamp(time)
        .setMaxBletchId(bletchId);
  }

  public GroupUpdate updateTenEven(double usd,
                                   double btc,
                                   long time,
                                   long bletchId) {

    update(IndexType.EVEN_INDEX, usd, btc, time, bletchId);
    return this;
  }

  public GroupUpdate updateTwenty(double usd,
                               double btc,
                               long time,
                                  long bletchId) {

    update(IndexType.INDEX_TWENTY, usd, btc, time, bletchId);
    return this;
  }

  public GroupUpdate updateTwentyEven(double usd,
                                   double btc,
                                   long time,
                                      long bletchId) {

    update(IndexType.EVEN_TWENTY, usd, btc, time, bletchId);
    return this;
  }

  public GroupUpdate updateEther(double usd,
                                   double btc,
                                   long time,
                                 long bletchId) {

    update(IndexType.INDEX_ETH, usd, btc, time, bletchId);
    return this;
  }

  public GroupUpdate updateEtherEven(double usd,
                                   double btc,
                                   long time,
                                     long bletchId) {

    update(IndexType.EVEN_ETH, usd, btc, time, bletchId);
    return this;
  }
}