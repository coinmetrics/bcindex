package com.frobro.bcindex.web.service.cache;


import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import com.frobro.bcindex.web.service.time.TimeService;

import java.util.concurrent.TimeUnit;

public class CacheLoader {
  private static final BcLog LOG = BcLog.getLogger(CacheLoader.class);

  private final DataCache cache;
  private final CacheUpdateMgr mgr;
  private final DbTickerService source;

  public CacheLoader(DataCache cache,
                     CacheUpdateMgr mgr,
                     DbTickerService db) {
    this.cache = cache;
    this.mgr = mgr;
    this.source = db;
  }

  public void load() {
    LOG.info("started loading cache ...");
    RequestDto req = new RequestDto();

    for (IndexType index : IndexType.values()) {
      req.index = index;

      for (TimeFrame timeFrame : TimeFrame.values()) {
        req.timeFrame = timeFrame;

        //        populate usd response
        req.currency = Currency.USD;
        long latestTime = cache.populateById(req, source);

        // if we have no data
        if (cache.noTimeExists(latestTime)) {
          latestTime = TimeService.currentTimeMillis();
        }

        // and btc [latest time should be the same as above
        req.currency = Currency.BTC;
        cache.populateById(req, source);

        mgr.loadExpiration(req.index, req.timeFrame, latestTime);
      }
    }

    try { Thread.sleep(TimeUnit.SECONDS.toMillis(40)); } catch (Exception e) {
      throw new RuntimeException(e);
    }

    cache.initCompleted();
    LOG.info("completed loading cache");
  }
}
