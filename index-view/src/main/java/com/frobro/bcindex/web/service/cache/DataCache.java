package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DataProvider;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.query.IndexUpdate;
import com.frobro.bcindex.web.service.time.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.frobro.bcindex.web.service.cache.DataNamer.createName;

public class DataCache {
  private static final Logger LOG = LoggerFactory.getLogger(DataCache.class);
  private static final long NO_TIME_EXISTS = -987654321;
  private final Map<String,ApiResponse> apiMap = new ConcurrentHashMap<>();
  private boolean isInit = Boolean.FALSE;

  /**
   * should remove the last element and
   * add the latest data to its list
   */
  void updateCache(Expiration exp, GroupUpdate update) {
    IndexUpdate newData = update.get(exp.getIndex());
    push(apiMap.get(getUsdKey(exp)), apiMap.get(getBtcKey(exp)), newData);
  }

  /**
   * should only overwrite the latest element
   * needed to keep all time frames consistent
   * with what the latest data is
   */
  void overwriteLatest(Expiration exp, GroupUpdate update) {
    LOG.debug("overwriting latest data for: "
        + createName(exp.getIndex(), exp.getTimeFrame()));
    IndexUpdate newData = update.get(exp.getIndex());
    overwrite(apiMap.get(getUsdKey(exp)), apiMap.get(getBtcKey(exp)), newData);
  }

  private void overwrite(ApiResponse usd, ApiResponse btc, IndexUpdate newData) {
    if (notNull(usd)) {
      usd.overWriteLastest(newData.getTimeStamp(), newData.getUsdPrice());
    }

    if (notNull(btc)) {
      btc.overWriteLastest(newData.getTimeStamp(), newData.getBtcPrice());
    }
  }

  private String getUsdKey(Expiration exp) {
    return createName(exp.getIndex(), exp.getTimeFrame(), Currency.USD);
  }

  private String getBtcKey(Expiration exp) {
    return createName(exp.getIndex(), exp.getTimeFrame(), Currency.BTC);
  }

  private void push(ApiResponse usd, ApiResponse btc, IndexUpdate newData) {
    if (notNull(usd)) {
      usd.addNewAndRemoveLast(newData.getTimeStamp(), newData.getUsdPrice());
    }

    if (notNull(btc)) {
      btc.addNewAndRemoveLast(newData.getTimeStamp(), newData.getBtcPrice());
    }
  }

  private boolean notNull(ApiResponse data) {
    boolean result = false;

    if (data != null) {
      result = true;
    }
    else {
      LOG.error("missing data in cache");
    }

    return result;
  }

  public String respondAsJson(RequestDto req) {
    return DbTickerService.toJson(respondTo(req));
  }

  public ApiResponse respondTo(RequestDto req) {
    String key = createName(req);
    ApiResponse resp = apiMap.get(key);

    if (resp == null) {
      LOG.warn("response is null for request: " + req
        + ", using default response");
      resp = createEmpty(req);
    }

    return resp;
  }

  private ApiResponse createEmpty(RequestDto req) {
    ApiResponse resp = ApiResponse.newResponse(req);
    double px = 0.0;
    long time = TimeService.currentTimeMillis();
    resp.lastPrice = px;
    resp.high = px;
    resp.low = px;
    resp.prevClose = px;
    resp.change = 0.0;
    resp.percentChange = 0.0;
    resp.updateFirst(px, time);
    resp.updateLast(px, time);
    resp.addData(px, time);
    return resp;
  }

  public void populateFromDb(DataProvider dataService) {
    RequestDto req = new RequestDto();

    for (IndexType index : IndexType.values()) {
      LOG.info("populating index: " + index.name());
      req.index = index;

      for (TimeFrame timeFrame : TimeFrame.values()) {
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

  public void initCompleted() {
    isInit = Boolean.TRUE;
  }

  public boolean isInitialized() {
    return isInit;
  }

  public boolean noTimeExists(long time) {
    return NO_TIME_EXISTS == time;
  }

  public long populateById(RequestDto req, DataProvider service) {
    ApiResponse response = service.getData(req);
    if (response.firstAndLastNotNull()) {
      apiMap.put(createName(req), response);
    }
    else {
      LOG.warn("not data exists for request: " + req);
    }
    return NO_TIME_EXISTS;
  }

  public void printKeys() {
    this.apiMap.entrySet().stream().forEach(System.out::println);
  }
}
