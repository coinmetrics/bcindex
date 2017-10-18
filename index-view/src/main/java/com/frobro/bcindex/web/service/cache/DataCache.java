package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DataProvider;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.query.IndexUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DataCache {
  private static final Logger LOG = LoggerFactory.getLogger(DataCache.class);
  private final Map<String,ApiResponse> apiMap = new ConcurrentHashMap<>();

  public void update() {
    // get index data from db
    TimeSeriesService seriesService = new TimeSeriesService();
    update(seriesService.getLastestData());
  }

  void update(GroupUpdate update) {
    // update each api response
    for (IndexType index : IndexType.values()) {
      for (TimeFrame frame : TimeFrame.values()) {
        if (frame == TimeFrame.QUARTERLY) continue;

        String key = createKey(index, frame, Currency.USD);
        ApiResponse data = apiMap.get(key);
        IndexUpdate updateData = update.get(index);

        long updateTime = updateData.getTimeStamp();
        if (notNull(data, key) && timeElapsed(data, updateTime)) {
          data.update(updateTime,
              updateData.getUsdPrice());
        }
      }
    }
  }

  // update only if the time has elapsed for this time frame
  public boolean timeElapsed(ApiResponse data, long updateTime) {
    long lastTime = data.getLatestTime();

    // if is max time frame
    if (data.timeFrame == TimeFrame.MAX) {
      long maxBletchId = ((MaxApiResponse)data).getMaxBletchId();
      return timeElapsed(lastTime, toTimeStep(maxBletchId));
    }

    return data.timeFrame.timeElapsed(
        data.timeFrame.round(updateTime) - lastTime);
  }

  private long toTimeStep(long maxBletchId) {
    long timeStep;
    if (maxBletchId < TimeFrame.DAILY.getNumDataPoints()) {
      timeStep = TimeFrame.HOURLY.getTimeStep();
    }
    else if (maxBletchId < TimeFrame.WEEKLY.getNumDataPoints()) {
      timeStep = TimeFrame.DAILY.getTimeStep();
    }
    else if (maxBletchId < TimeFrame.MONTHLY.getNumDataPoints()) {
      timeStep = TimeFrame.MONTHLY.getTimeStep();
    }
    else {
      timeStep = TimeFrame.QUARTERLY.getTimeStep();
    }
    return timeStep;
  }

  private boolean timeElapsed(long timeDiff, long timeStep) {
    return timeDiff > TimeUnit.MINUTES.toMillis(timeStep);
  }

  private boolean notNull(ApiResponse data, String key) {
    boolean result = false;

    if (data != null) {
      result = true;
    }
    else {
      LOG.error("no data for key: " + key);
    }

    return result;
  }

  public ApiResponse respondTo(RequestDto req) {
    String key = createKey(req);
    ApiResponse resp = apiMap.get(key);

    if (req == null) {
      throw new IllegalStateException("no response for: " + req);
    }
    return resp;
  }

  public String respondAsJson(RequestDto req) {
    return DbTickerService.toJson(respondTo(req));
  }

  public void populateFromDb(DataProvider dataService) {
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

  private void populateById(RequestDto req, DataProvider service) {
    ApiResponse response = service.getData(req);
    if (response.firstAndLastNotNull()) {
      apiMap.put(createKey(req), response);
    }
    else {
      LOG.warn("not data exists for request: " + req);
    }
  }

  private String createKey(RequestDto req) {
    return createKey(req.index, req.timeFrame, req.currency);
  }

  private String createKey(IndexType index, TimeFrame frame, Currency currency) {
    return index.name() + "." + frame.name() + "." + currency;
  }
}
