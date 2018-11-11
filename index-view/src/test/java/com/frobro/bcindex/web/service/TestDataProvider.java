package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.time.TimeService;

import java.util.HashMap;
import java.util.Map;

public class TestDataProvider implements DataProvider {
  private static final int SIZE = 20;
  private static final Map<String,Integer> multMap = new HashMap<>();
  static {
    // indexes
    int mult = 10;
    for (IndexType idx : IndexType.values()) {
      multMap.put(idx.name(), mult);
      mult += 10;
    }
    // currencies
    multMap.put(Currency.USD.name(), 2);
    multMap.put(Currency.BTC.name(), 1);

    int curCnt = 1;
    for (Currency currency : Currency.values()) {
      multMap.put(currency.name(), curCnt);
      curCnt++;
    }

    int timeCnt = 5;
    for (TimeFrame frame : TimeFrame.values()) {
      multMap.put(frame.name(), timeCnt);
      timeCnt++;
    }
  }

  @Override
  public ApiResponse getData(RequestDto req) {
    ApiResponse resp = ApiResponse.newResponse(req);


    // set total count to avoid later NPE
    if (req.timeFrame == TimeFrame.MAX) {
      ((MaxApiResponse)resp).setTotalCount(0);
    }

    addPriceAndTime(resp, createUniqueMultiplier(req));
    resp.calcAndFormatData();
    return resp;
  }

  private int createUniqueMultiplier(RequestDto req) {
    int idxM = multMap.get(req.index.name());
    int curM = multMap.get(req.currency.name());
    int time = multMap.get(req.timeFrame.name());
    return idxM + curM - time;
  }

  private void addPriceAndTime(ApiResponse resp, int multiplier) {
    long now = TimeService.currentTimeMillis();
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