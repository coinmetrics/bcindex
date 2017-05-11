package com.frobro.bcindex.web.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rise on 4/9/17.
 */
@Entity
public class JpaIndex {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private Date timeStamp;
  private double indexValueUsd;
  private double indexValueBtc;

  public double getIndexValueUsd() {
    return indexValueUsd;
  }

  public double getIndexValueBtc() {
    return indexValueBtc;
  }

  public static JpaIndex create() {
    JpaIndex idx = new JpaIndex();
    setTime(idx);
    return idx;
  }

  protected static void setTime(JpaIndex idx) {
    idx.setTimeStamp(new Date(System.currentTimeMillis()));
  }

  /* needed for db serialization */
  public JpaIndex() {}

  public Long getId() {
    return id;
  }

  public JpaIndex setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public JpaIndex setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

  public JpaIndex setIndexValueUsd(double indexValueUsd) {
    this.indexValueUsd = indexValueUsd;
    return this;
  }

  public JpaIndex setIndexValueBtc(double indexValueBtc) {
    this.indexValueBtc = indexValueBtc;
    return this;
  }

  @Override
  public String toString() {
    return "JpaIndex{" +
        "id=" + id +
        ", timeStamp=" + timeStamp +
        ", indexValueUsd=" + indexValueUsd +
        ", indexValueBtc=" + indexValueBtc +
        '}';
  }
}
