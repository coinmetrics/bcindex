package com.frobro.bcindex.core.db.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightApi {
  private static final String KEY = "key";
  private static final String SECRET = "fkdjfkdjfdjfkjfdk";
  Map<String,Map<String,Double>> allIndexes = new HashMap<>();

  public void addKey() {
    Map<String,Double> keyMap = new HashMap<>();
    keyMap.put(SECRET,0.0);
    allIndexes.put(KEY, keyMap);
  }

  public static boolean isSecure(Map<String,Map<String,Double>> map) {
    boolean isSecure = Boolean.FALSE;
    Map<String,Double> entry = map.get(KEY);
    if (entry != null && entry.containsKey(SECRET)) {
      isSecure = Boolean.TRUE;
    }
    return isSecure;
  }

  public static void removeKeyIfExists(Map<String,Map<String,Double>> map) {
    map.remove(KEY);
  }

  public void addIndex(IndexName name, Map<String,Double> weights) {
    allIndexes.put(name.name(), weights);
  }

  public void update(Map<String,Map<String,Double>> data) {
    allIndexes = new HashMap<>(data);
  }

  public Map<String,Map<String,Double>> getRawData() {
    return new HashMap<>(allIndexes);
  }

  public Map<String,Double> getWeight(IndexName index) {
    return allIndexes.get(index.name());
  }

  public Map<String,Map<String,Double>> getWeight(List<IndexName> indexes) {
    final Map<String,Map<String,Double>> response = new HashMap<>();

    indexes.forEach(index -> {
      Map<String,Double> weights = allIndexes.get(index.name());
      if (weights == null) {
        throw new RuntimeException("no data found for index: " + index);
      }
      response.put(index.name(),weights);
    });
    return response;
  }

  @Override
  public String toString() {
    final StringBuilder state = new StringBuilder();
    final String lineBreak = "\n";

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
}
