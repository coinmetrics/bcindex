package com.frobro.bcindex.core.model;

import java.util.HashMap;
import java.util.Map;

public class WeightDto {
  public long time;
  public String key;
  public Map<IndexName,Map<String,Double>> indexes = new HashMap<>();
}
