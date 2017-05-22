package com.frobro.bcindex.web.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rise on 4/9/17.
 */
@Entity
@Table(name = "odd_index")
public class JpaIndex {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    return idx;
  }

  /* needed for db serialization */
  public JpaIndex() {}

  public JpaIndex setTimeStamp(long time) {
    timeStamp = new Date(time);
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
