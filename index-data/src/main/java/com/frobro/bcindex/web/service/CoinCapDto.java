package com.frobro.bcindex.web.service;

/**
 * Created by rise on 4/22/17.
 */
public class CoinCapDto {
  private double btcPrice;
  private double btcCap;
  private long altCap;
  private int dom;
  private long bitnodesCount;

  public double getBtcPrice() {
    return btcPrice;
  }

  public void setBtcPrice(double btcPrice) {
    this.btcPrice = btcPrice;
  }

  public double getBtcCap() {
    return btcCap;
  }

  public void setBtcCap(double btcCap) {
    this.btcCap = btcCap;
  }

  public long getAltCap() {
    return altCap;
  }

  public void setAltCap(long altCap) {
    this.altCap = altCap;
  }

  public int getDom() {
    return dom;
  }

  public void setDom(int dom) {
    this.dom = dom;
  }

  public long getBitnodesCount() {
    return bitnodesCount;
  }

  public void setBitnodesCount(long bitnodesCount) {
    this.bitnodesCount = bitnodesCount;
  }
}
