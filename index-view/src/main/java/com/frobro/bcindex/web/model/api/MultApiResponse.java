package com.frobro.bcindex.web.model.api;

import java.util.LinkedList;
import java.util.List;

/**
 * holds multiple API responses to support
 * plots with multiple data
 */
public class MultApiResponse {
  public final List<Double> firstPlotPrices = new LinkedList<>();
  public List<String> firstPlotTimes = new LinkedList<>();

  public final List<Double> secondPlotPrices = new LinkedList<>();
  public List<String> secondPlotTimes = new LinkedList<>();
}
