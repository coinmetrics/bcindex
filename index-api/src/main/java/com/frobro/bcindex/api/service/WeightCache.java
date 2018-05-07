package com.frobro.bcindex.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.core.db.model.IndexName;

import java.util.HashMap;
import java.util.Map;

public class WeightCache {
  ObjectMapper mapper = new ObjectMapper();
  Map<IndexName,String> cache = new HashMap<>();

  public String getResonse(IndexName indexName) {
    return cache.get(indexName);
  }

  public void update(JsonData jsonData) {
    cache.put(jsonData.indexName,toString(jsonData));
  }

  private String toString(JsonData jsonData) {
    try {
      return mapper.writeValueAsString(jsonData);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe);
    }
  }
}
