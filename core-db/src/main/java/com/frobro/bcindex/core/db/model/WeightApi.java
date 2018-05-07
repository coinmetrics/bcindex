package com.frobro.bcindex.core.db.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightApi {
  private static final String SECRET = "fkdjfkdjfdjfkjfdk";
  private long time;
  private String key;
  Map<IndexName,Map<String,Double>> allIndexes = new HashMap<>();

  public void addKey() {
    key = SECRET;
  }

  public boolean amSecure() {
    boolean isSecure = Boolean.FALSE;
    if (key != null && key.equals(SECRET)) {
      isSecure = Boolean.TRUE;
    }
    return isSecure;
  }

  public void removeKeyIfExists() {
    key = null;
  }

  public void addIndex(IndexName name, Map<String,Double> weights) {
    allIndexes.put(name, weights);
  }

  public void update(long time, Map<IndexName,Map<String,Double>> data) {
    this.time = time;
    this.allIndexes = new HashMap<IndexName,Map<String,Double>>(data);
  }

  public long getTime() {
    return time;
  }

  public void setRawData(Map<IndexName,Map<String,Double>> map) {
    this.allIndexes = map;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Map<IndexName,Map<String,Double>> getRawData() {
    return new HashMap<>(allIndexes);
  }

  public Map<String,Double> getWeight(IndexName index) {
    return allIndexes.get(index);
  }

  public Map<IndexName,Map<String,Double>> getWeight(List<IndexName> indexes) {
    final Map<IndexName,Map<String,Double>> response = new HashMap<>();

    indexes.forEach(index -> {
      Map<String,Double> weights = allIndexes.get(index);
      if (weights == null) {
        throw new RuntimeException("no data found for index: " + index);
      }
      response.put(index,weights);
    });
    return response;
  }

  @Override
  public String toString() {
    final StringBuilder state = new StringBuilder();
    final String lineBreak = "\n";

    state.append("time: ").append(time).append(lineBreak);
    allIndexes.entrySet().forEach(entry -> {
      state.append(entry.getKey()).append(lineBreak);
      entry.getValue().entrySet().forEach(v -> {
        state.append("  " + v.getKey() + "=" + v.getValue()).append(lineBreak);
      });
    });

    state.append("number indices: " + allIndexes.keySet().size()).append(lineBreak);
    state.append(lineBreak);

    return state.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WeightApi weightApi = (WeightApi) o;

    if (getTime() != weightApi.getTime()) return false;
    return allIndexes != null ? allIndexes.equals(weightApi.allIndexes) : weightApi.allIndexes == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getTime() ^ (getTime() >>> 32));
    result = 31 * result + (allIndexes != null ? allIndexes.hashCode() : 0);
    return result;
  }
}
