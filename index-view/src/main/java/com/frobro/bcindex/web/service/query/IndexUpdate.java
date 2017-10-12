package com.frobro.bcindex.web.service.query;

public class IndexUpdate {
  private Double usdPrice;
  private Double btcPrice;
  private long timeStamp;

  public Double getUsdPrice() {
    return usdPrice;
  }

  public IndexUpdate setUsdPrice(Double usdPrice) {
    this.usdPrice = usdPrice;
    return this;
  }

  public Double getBtcPrice() {
    return btcPrice;
  }

  public IndexUpdate setBtcPrice(Double btcPrice) {
    this.btcPrice = btcPrice;
    return this;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public IndexUpdate setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }
}