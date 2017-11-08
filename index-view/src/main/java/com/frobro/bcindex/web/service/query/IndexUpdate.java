package com.frobro.bcindex.web.service.query;

public class IndexUpdate {
  // need to populate this
  private Long maxBletchId;
  private Double usdPrice;
  private Double btcPrice;
  private Long timeStamp;

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

  public IndexUpdate setMaxBletchId(long maxId) {
    this.maxBletchId = maxId;
    return this;
  }

  public long getMaxBletchId() {
    return maxBletchId.longValue();
  }
}