package com.frobro.bcindex.api.model;

import java.util.Map;

public class JsonElement implements Comparable<JsonElement> {
  public long time;
  public Map<String,Double> dataMap;

  @Override
  public int compareTo(JsonElement o) {
    long diff = time - o.time;

    if (diff > 0) {
      return 1;
    }
    else if (diff == 0) {
      return 0;
    }
    else {
      return -1;
    }
  }

  @Override
  public String toString() {
    return "JsonElement{" +
        "time=" + time +
        ", dataMap=" + dataMap +
        '}';
  }
}
