package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.model.api.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class TestDataProvider implements DataProvider {
  private static final int SIZE = 20;
  private static final Map<String,Integer> multMap = new HashMap<>();
  static {
    // indexes
    multMap.put(IndexType.ODD_INDEX.name(), 10);
    multMap.put(IndexType.EVEN_INDEX.name(), -10);
    multMap.put(IndexType.INDEX_TWENTY.name(), 20);
    multMap.put(IndexType.EVEN_TWENTY.name(), -20);
    multMap.put(IndexType.INDEX_ETH.name(), 30);
    multMap.put(IndexType.EVEN_ETH.name(), -30);
    // currencies
    multMap.put(Currency.USD.name(), 2);
    multMap.put(Currency.BTC.name(), 1);
    // time frame
    multMap.put(TimeFrame.HOURLY.name(), 10);
    multMap.put(TimeFrame.DAILY.name(), 9);
    multMap.put(TimeFrame.WEEKLY.name(), 8);
    multMap.put(TimeFrame.MONTHLY.name(), 7);
    multMap.put(TimeFrame.MAX.name(), 60);
  }
  private int multiplier;

  @Override
  public ApiResponse getData(RequestDto req) {
    ApiResponse resp = ApiResponse.newResponse(req);
    multiplier = createUniqueMultiplier(req);
    addPriceAndTime(resp);
    resp.calcAndFormatData();
    return resp;
  }

  private int createUniqueMultiplier(RequestDto req) {
    int idxM = multMap.get(req.index.name());
    int curM = multMap.get(req.currency.name());
    int time = multMap.get(req.timeFrame.name());
    return idxM + curM - time;
  }

  private void addPriceAndTime(ApiResponse resp) {
    long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    double low = 1.0;
    double high = new Double(SIZE * multiplier);
    resp.updateFirst(low, (now-1));
    for (int i=0; i<SIZE; i++) {
      resp.addData(new Double(i * multiplier), now + i);
    }
    resp.updateLast(new Double(SIZE * multiplier), (now + SIZE));
    resp.setMaxAndMinPrice(high,low);
  }
}