package com.frobro.bcindex.web.model;

/**
 * Created by rise on 4/24/17.
 */
public class Ticker {
  private final String name;
  private Double marketCap;
  private Double evenMultiplier;

  public Ticker(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Double getMarketCap() {
    return marketCap;
  }

  public Ticker setMarketCap(double marketCap) {
    this.marketCap = marketCap;
    return this;
  }

  public Double getEvenMultiplier() {
    return evenMultiplier;
  }

  public Ticker setEvenMultiplier(double evenMultiplier) {
    this.evenMultiplier = evenMultiplier;
    return this;
  }
}