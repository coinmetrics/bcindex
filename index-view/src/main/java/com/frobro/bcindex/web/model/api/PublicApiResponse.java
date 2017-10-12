package com.frobro.bcindex.web.model.api;

import java.util.LinkedList;
import java.util.List;

public class PublicApiResponse {
  public PublicIndex index;
  public Currency currency;
  public PublicTimeFrame timeFrame;

  public double lastPrice;
  public Double high;
  public Double low;
  public Double prevClose;
  public Double change;
  public Double percentChange;

  // graph data
  public List<Double> data = new LinkedList<>();
  public List<Long> times = new LinkedList<>();
}
