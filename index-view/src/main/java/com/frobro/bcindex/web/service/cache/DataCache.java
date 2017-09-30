package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataCache {
  private static final Logger LOG = LoggerFactory.getLogger(DataCache.class);

  private final Map<String,ApiResponse> apiMap = new ConcurrentHashMap<>();

  private static ApiResponse getTenUsdDto(TimeFrame frame) {
    RequestDto dto = new RequestDto();
    dto.currency = Currency.USD;
    dto.timeFrame = frame;
    dto.index = IndexType.ODD_INDEX;
    return ApiResponse.newResponse(dto);
  }

  private static ApiResponse getTenBtcDto(TimeFrame frame) {
    RequestDto dto = new RequestDto();
    dto.currency = Currency.BTC;
    dto.timeFrame = frame;
    dto.index = IndexType.ODD_INDEX;
    return ApiResponse.newResponse(dto);
  }

  public void update() {
    for (IndexType index : IndexType.values()) {

    }
  }

  public ApiResponse respondTo(RequestDto req) {
    ApiResponse resp = apiMap.get(createKey(req));

    if (req == null) {
      throw new IllegalStateException("no response for: " + req);
    }
    return resp;
  }

  private void populate() {
  }

  public String respondAsJson(RequestDto req) {
    return DbTickerService.toJson(respondTo(req));
  }

  public void populateFromDb(DbTickerService dataService) {
    RequestDto req = new RequestDto();

    for (IndexType index : IndexType.values()) {
      LOG.info("populating index: " + index.name());
      req.index = index;


      for (TimeFrame timeFrame : TimeFrame.values()) {
        if (timeFrame == TimeFrame.QUARTERLY) continue;

        LOG.info("populating time frame: " + timeFrame.name());

        req.timeFrame = timeFrame;
        //        populate usd response
        req.currency = Currency.USD;
        populateById(req, dataService);

        // and btc
        req.currency = Currency.BTC;
        populateById(req, dataService);
      }
    }
  }

  private void populateById(RequestDto req, DbTickerService service) {
    ApiResponse response = service.respond(req);
    apiMap.put(createKey(req), response);
  }

  private void populateByTime(IndexType index, TimeFrame timeFrame) {
//    CsvTimeQuery query = new CsvTimeQuery(jdbc, timeFrame.getTimeStep());
//    // create usd
//    ApiResponse usd = ApiResponse.newResponse(index, timeFrame, Currency.USD);
//    // create btc
//    ApiResponse btc = ApiResponse.newResponse(index, timeFrame, Currency.BTC);
//    // make the db call and populate
//    query.getCacheContent(index.name(), timeFrame, usd, btc);
//    // create usd string and put
//    apiMap.put(createKey(usd.index, usd.timeFrame, usd.currency),usd);
//    // create btc string and put
//    apiMap.put(createKey(btc.index, btc.timeFrame, btc.currency),btc);
  }

  private String createKey(RequestDto req) {
    return createKey(req.index, req.timeFrame, req.currency);
  }

  private String createKey(IndexType index, TimeFrame frame, Currency currency) {
    return index.name() + "." + frame.name() + "." + currency;
  }

  private double formatUsd(double d) {
    return Currency.USD.format(d);
  }

  private double formatBtc(double d) {
    return Currency.BTC.format(d);
  }
}
