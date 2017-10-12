package com.frobro.bcindex.web.service.query;

public class GroupUpdate {
  private IndexUpdate ten;
  private IndexUpdate tenEven;
  private IndexUpdate twenty;
  private IndexUpdate twentyEven;
  private IndexUpdate ether;
  private IndexUpdate etherEven;

  public GroupUpdate updateTen(double usd,
                               double btc,
                               long time) {
    ten = newUpdate(usd, btc, time);
    return this;
  }

  private IndexUpdate newUpdate(double usd, double btc,
                                long time) {
    return new IndexUpdate()
        .setUsdPrice(usd)
        .setBtcPrice(btc)
        .setTimeStamp(time);
  }

  public GroupUpdate updateTenEven(double usd,
                                   double btc,
                                   long time) {
    tenEven = newUpdate(usd, btc, time);
    return this;
  }

  public GroupUpdate updateTwenty(double usd,
                               double btc,
                               long time) {
    twenty = newUpdate(usd, btc, time);
    return this;
  }

  public GroupUpdate updateTwentyEven(double usd,
                                   double btc,
                                   long time) {
    twentyEven = newUpdate(usd, btc, time);
    return this;
  }

  public GroupUpdate updateEther(double usd,
                                   double btc,
                                   long time) {
    ether = newUpdate(usd, btc, time);
    return this;
  }

  public GroupUpdate updateEtherEven(double usd,
                                   double btc,
                                   long time) {
    etherEven = newUpdate(usd, btc, time);
    return this;
  }

  public IndexUpdate getTen() {
    return ten;
  }

  public IndexUpdate getTenEven() {
    return tenEven;
  }

  public IndexUpdate getTwenty() {
    return twenty;
  }

  public IndexUpdate getTwentyEven() {
    return twentyEven;
  }

  public IndexUpdate getEther() {
    return ether;
  }

  public IndexUpdate getEtherEven() {
    return etherEven;
  }
}