package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.core.model.IndexName;

import java.util.HashMap;
import java.util.Map;

public class WeightCache {
  Map<IndexName,JsonData> cache = new HashMap<>();

  public JsonData getResonse(IndexName indexName) {
    return cache.get(indexName);
  }

  public void update(JsonData jsonData) {
    cache.put(jsonData.indexName,jsonData);
  }
}
