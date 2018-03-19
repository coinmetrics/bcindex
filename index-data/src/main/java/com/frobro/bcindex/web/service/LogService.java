package com.frobro.bcindex.web.service;

import com.frobro.bcindex.core.db.service.DoubleService;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

import java.util.Map;

public class LogService {
  private static final String LINE_BREAK = "\n";
  private static final int TAG_SPACES = 14;
  private static final String SPACE = " ";
  private static final String INDEX_SEPARATOR = "-----------------------------";
  private StringBuilder builder = new StringBuilder();

  public String getLogMessage() {
    return builder.toString();
  }

  public LogService addAllWeights(String name, IndexCalculator calculator) {
    adWeightsWithEven(name, calculator.getWeights(), calculator.getWeightsEven());
    builder.append(LINE_BREAK);
    return this;
  }

  private void lineSeparator() {
    builder.append(INDEX_SEPARATOR).append(LINE_BREAK);
  }

  public LogService addSingleWeights(String name, Map<String,Double> weights) {
    addWeights(name, weights);
    builder.append(LINE_BREAK);
    return this;
  }

  private void adWeightsWithEven(String name, Map<String,Double> weights,
                                 Map<String,Double> weightsEven) {

    int size = weights.size();
    if (size != weightsEven.size()) {
      throw new IllegalStateException("weights size: " + size +
      " must be equal to even weights size: " + weightsEven.size());
    }

    builder.append(name + " Weights:").append(LINE_BREAK);

    for (String key : weights.keySet()) {
      builder.append(key).append("=").append(format(weights.get(key)))
          .append(SPACE).append(SPACE)
          .append(format(weightsEven.get(key)))
          .append(LINE_BREAK);
    }
  }

  private double format(double realNumber) {
    return DoubleService.formatWeight(realNumber);
  }

  private void addWeights(String name, Map<String,Double> weights) {
    builder.append(name + " Weights:").append(LINE_BREAK);
    weights.entrySet().forEach(entry -> {
      builder.append(entry.getKey()).append("=").append(format(entry.getValue()))
          .append(LINE_BREAK);
    });
  }

  public LogService addIndexValue(String name, IndexDbDto dto, IndexDbDto dtoEven) {
    addIndexValue(name, dto);
    addIndexValue(evenName(name), dtoEven);
    lineSeparator();
    return this;
  }

  public LogService addIndexValue(String name, IndexDbDto dto) {
    builder.append(name).append(formatSpaces(name))
        .append("[btc=").append(dto.indexValueBtc)
        .append(", ").append("usd=").append(dto.indexValueUsd)
        .append("]").append(LINE_BREAK);
    return this;
  }

  private String evenName(String name) {
    return name + " Even";
  }

  private String formatSpaces(String name) {
    int diff = TAG_SPACES - name.length();
    if (diff < 1) {
      throw new IllegalStateException("name is long than tab. please update tab");
    }

    StringBuilder spaces = new StringBuilder();
    for (int i=0; i<diff; i++) {
      spaces.append(SPACE);
    }
    return spaces.toString();
  }
}
