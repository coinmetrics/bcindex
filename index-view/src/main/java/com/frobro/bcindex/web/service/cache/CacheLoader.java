package com.frobro.bcindex.web.service.cache;


import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.DbTickerService;

public class CacheLoader {
  private static final BcLog LOG = BcLog.getLogger(CacheLoader.class);

  public static void load(DataCache cache,
                          CacheUpdateMgr mgr,
                          DbTickerService source) {

    RequestDto req = new RequestDto();

    for (IndexType index : IndexType.values()) {
      req.index = index;

      for (TimeFrame timeFrame : TimeFrame.values()) {
        req.timeFrame = timeFrame;
        //        populate usd response
        req.currency = Currency.USD;
        cache.populateById(req, source);
        mgr.loadExpiration(req, source);

        // and btc
        req.currency = Currency.BTC;
        cache.populateById(req, source);
        mgr.loadExpiration(req, source);
      }
    }
  }
}
