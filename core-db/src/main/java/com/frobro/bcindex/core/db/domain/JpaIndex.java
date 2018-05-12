package com.frobro.bcindex.core.db.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by rise on 4/9/17.
 */
@MappedSuperclass
public class JpaIndex {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private long bletchId;

  private long timeStamp;
  private double indexValueUsd;
  private double indexValueBtc;

  public double getIndexValueUsd() {
    return indexValueUsd;
  }

  public double getIndexValueBtc() {
    return indexValueBtc;
  }

  public static JpaIndex create() {
    JpaIndex idx = new JpaIndexTen();
    return idx;
  }

  /* needed for db serialization */
  public JpaIndex() {}

  public JpaIndex setTimeStamp(long time) {
    timeStamp = time;
    return this;
  }

  public long getBletchId() {
    return bletchId;
  }

  public JpaIndex setBletchId(long id) {
    this.bletchId = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public JpaIndex setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getTimeStamp() {
    return new Date(timeStamp);
  }

  public JpaIndex setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp.getTime();
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
