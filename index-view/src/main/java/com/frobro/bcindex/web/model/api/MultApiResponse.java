package com.frobro.bcindex.web.model.api;

import java.util.LinkedList;
import java.util.List;

/**
 * holds multiple API responses to support
 * plots with multiple data
 */
public class MultApiResponse {
  public IndexType index;
  public Currency currency;
  public TimeFrame timeFrame;
  public String timeUnit;

  public double lastPrice = -1.0;
  public Double high;
  public Double low;
  public Double prevClose;
  public Double change;
  public Double percentChange;

  public final List<List<Double>> prices = new LinkedList<>();
  public final List<List<String>> times = new LinkedList<>();
}
