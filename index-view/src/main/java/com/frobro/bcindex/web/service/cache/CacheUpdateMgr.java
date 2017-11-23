package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.DataProvider;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.frobro.bcindex.web.service.cache.DataNamer.createName;

/**
 * Keep the cache up to date
 */
public class CacheUpdateMgr {
  private static final Logger LOG = LoggerFactory.getLogger(CacheUpdateMgr.class);

  private final Map<IndexType, List<Expiration>> expireMap = new HashMap<>();
  private final DataCache cache;
  private final TimeSeriesService dbService;

  public CacheUpdateMgr(DataCache cache, TimeSeriesService service) {
    this.cache = cache;
    this.dbService = service;
    initExpireMap();
  }

  private void initExpireMap() {
    for (IndexType index : IndexType.values()) {
      expireMap.put(index, new ArrayList<>(TimeFrame.values().length));
    }
  }

  public void loadExpiration(RequestDto req, DataProvider dataProvider) {
    ApiResponse response = dataProvider.getData(req);
    if (response.firstAndLastNotNull()) {
      Expiration exp = new Expiration(req.index, req.timeFrame);
      exp.updateLastTime(response.getLatestTime());

      List<Expiration> expList = expireMap.get(req.index);
      expList.add(exp);
    }
    else {
      LOG.warn("no data exists for request: " + req);
    }
  }

  public void set(Map<IndexType,List<Expiration>> map) {
    expireMap.putAll(map);
  }

  public void update() {
    LOG.debug("getting new data from the data base");
    update(dbService.getLastestData());
  }

  public boolean isInit() {
    return cache.isInitialized();
  }

  Set<String> update(GroupUpdate update) {

    Set<String> updated = new HashSet<>();

    for (IndexType index : update.getIndices()) {
      List<Expiration> expires = get(index);

      for (Expiration exp : expires) {
        if (exp.isExpired(update.get(index))) {
          // remove oldest and add new to list
          cache.updateCache(exp, update);
          updated.add(createName(index,exp.getTimeFrame()));
          exp.updateLastTime(update.getTime(index));
        }
        else {
          // overwrite the latest data with the new data
          cache.overwriteLatest(exp, update);
        }
      }
    }
    return updated;
  }

  private List<Expiration> get(IndexType index) {
    List<Expiration> list = expireMap.get(index);
    return list == null ? Collections.emptyList() : list;
  }
}