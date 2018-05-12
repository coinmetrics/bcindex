package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.TimeFrame;
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

  public String expirationsToString() {
    StringBuilder msg = new StringBuilder();
    for (IndexType idx : expireMap.keySet()) {
      msg.append(expireMap.get(idx).toString());
    }
    return msg.toString();
  }

  public void loadExpiration(IndexType index, TimeFrame frame, long latestTime) {
      Expiration exp = new Expiration(index, frame);
      exp.updateLastTime(latestTime);

      List<Expiration> expList = expireMap.get(index);
      expList.add(exp);
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
        String label = exp.getLabel();

        if (exp.isExpired(update.get(index))) {
          LOG.debug(label + " has expired");
          cache.updateCache(exp, update);
          updated.add(createName(index,exp.getTimeFrame()));
          exp.updateLastTime(update.getTime(index));
        }

        else {
          // overwrite the latest data with the new data
          cache.overwriteLatest(exp, update);
          LOG.debug(label + " overwriting last data point");
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