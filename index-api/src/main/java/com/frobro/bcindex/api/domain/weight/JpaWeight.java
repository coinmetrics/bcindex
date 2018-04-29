package com.frobro.bcindex.api.domain.weight;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class JpaWeight {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private long timeStamp;
  private String ticker;
  private double weight;
  private String indexName;

  public String getIndexName() {
    return indexName;
  }

  public JpaWeight setIndexName(String indexName) {
    this.indexName = indexName;
    return this;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public JpaWeight setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

  public String getTicker() {
    return ticker;
  }

  public JpaWeight setTicker(String ticker) {
    this.ticker = ticker;
    return this;
  }

  public double getWeight() {
    return weight;
  }

  public JpaWeight setWeight(double weight) {
    this.weight = weight;
    return this;
  }
}
