package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.domain.Index;

import java.util.HashMap;
import java.util.Map;

public class Weight {

  public static Map<String,Double> calculateWeight(BletchleyData data, double sum) {
    return calculateWeight(data.getLastIndexes(), sum);
  }

  public static Map<String,Double> calculateWeight(Map<String,Index> dataMap, double sum) {
    final Map<String,Double> weights = new HashMap<>();

    for (Index idx : dataMap.values()) {
      weights.put(idx.getName(), idx.getMktCap()/sum);
    }
    return weights;
  }
}
