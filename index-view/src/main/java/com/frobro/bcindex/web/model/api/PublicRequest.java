package com.frobro.bcindex.web.model.api;

public class PublicRequest {
  public String apiKey;
  public PublicIndex index;
  public Currency currency;
  public PublicTimeFrame timeFrame;

  @Override
  public String toString() {
    return "PublicRequest{" +
        "index=" + index +
        ", currency=" + currency +
        ", timeFrame=" + timeFrame +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PublicRequest)) return false;

    PublicRequest that = (PublicRequest) o;

    if (index != that.index) return false;
    if (currency != that.currency) return false;
    return timeFrame == that.timeFrame;

  }

  @Override
  public int hashCode() {
    int result = index != null ? index.hashCode() : 0;
    result = 31 * result + (currency != null ? currency.hashCode() : 0);
    result = 31 * result + (timeFrame != null ? timeFrame.hashCode() : 0);
    return result;
  }
}
