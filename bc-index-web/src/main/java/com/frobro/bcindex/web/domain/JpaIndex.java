package com.frobro.bcindex.web.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by rise on 4/9/17.
 */
@Entity
public class JpaIndex {

  @Id
  @GeneratedValue
  private long id;

  private Date date;
  private double indexValueUsd;
  private double usdPerBtc;

  public double getIndexValueUsd() {
    return indexValueUsd;
  }

  public double getUsdPerBtc() {
    return usdPerBtc;
  }

  /* needed for db serialization */
  public JpaIndex() {}

  public JpaIndex(double indexValueUsd, double usdPerBtc) {
    this.indexValueUsd = indexValueUsd;
    this.usdPerBtc = usdPerBtc;
    this.date = new Date(System.currentTimeMillis());
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setIndexValueUsd(double indexValueUsd) {
    this.indexValueUsd = indexValueUsd;
  }

  public void setUsdPerBtc(double usdPerBtc) {
    this.usdPerBtc = usdPerBtc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JpaIndex)) return false;

    JpaIndex jpaIndex = (JpaIndex) o;

    if (id != jpaIndex.id) return false;
    if (Double.compare(jpaIndex.indexValueUsd, indexValueUsd) != 0) return false;
    if (Double.compare(jpaIndex.usdPerBtc, usdPerBtc) != 0) return false;
    return !(date != null ? !date.equals(jpaIndex.date) : jpaIndex.date != null);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (id ^ (id >>> 32));
    result = 31 * result + (date != null ? date.hashCode() : 0);
    temp = Double.doubleToLongBits(indexValueUsd);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(usdPerBtc);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "JpaIndex{" +
        "id=" + id +
        ", date=" + date +
        ", indexValueUsd=" + indexValueUsd +
        ", usdPerBtc=" + usdPerBtc +
        '}';
  }
}
