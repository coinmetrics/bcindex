package com.frobro.bcindex.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightApi {
  private static final String SECRET = "fkdjfkdjfdjfkjfdk";
  private long time;
  private String key;
  private Map<IndexName,Map<String,Double>> indexes = new HashMap<>();

  public WeightApi() {
    key = SECRET;
  }

  public WeightApi(WeightDto dto) {
    this.time = dto.time;
    this.indexes = dto.indexes;
    this.key = dto.key;
  }

  public boolean amSecure() {
    boolean isSecure = Boolean.FALSE;
    if (key != null && key.equals(SECRET)) {
      isSecure = Boolean.TRUE;
    }
    return isSecure;
  }

  public WeightDto getWeightDto() {
    WeightDto dto = new WeightDto();
    dto.key = SECRET;
    dto.time = time;
    dto.indexes = indexes;
    return dto;
  }

  public void removeKeyIfExists() {
    key = null;
  }

  public void addIndex(IndexName name, Map<String,Double> weights) {
    indexes.put(name, weights);
  }

  public void update(long time, Map<IndexName,Map<String,Double>> data) {
    this.time = time;
    this.indexes = new HashMap<IndexName,Map<String,Double>>(data);
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Set<IndexName> getIndexes() {
    return indexes.keySet();
  }

  public Map<String,Double> getWeight(IndexName index) {
    return indexes.get(index);
  }

  public Map<IndexName,Map<String,Double>> getWeight(List<IndexName> indexes) {
    final Map<IndexName,Map<String,Double>> response = new HashMap<>();

    indexes.forEach(index -> {
      Map<String,Double> weights = this.indexes.get(index);
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
    indexes.entrySet().forEach(entry -> {
      state.append(entry.getKey()).append(lineBreak);
      entry.getValue().entrySet().forEach(v -> {
        state.append("  " + v.getKey() + "=" + v.getValue()).append(lineBreak);
      });
    });

    state.append("number indices: " + indexes.keySet().size()).append(lineBreak);
    state.append(lineBreak);

    return state.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WeightApi weightApi = (WeightApi) o;

    if (getTime() != weightApi.getTime()) return false;
    return indexes != null ? indexes.equals(weightApi.indexes) : weightApi.indexes == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (getTime() ^ (getTime() >>> 32));
    result = 31 * result + (indexes != null ? indexes.hashCode() : 0);
    return result;
  }
}
