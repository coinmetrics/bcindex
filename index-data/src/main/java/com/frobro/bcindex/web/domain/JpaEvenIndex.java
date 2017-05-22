package com.frobro.bcindex.web.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rise on 5/9/17.
 */
@Entity
@Table(name = "even_index")
public class JpaEvenIndex {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  private Date timeStamp;
  private double indexValueUsd;
  private double indexValueBtc;

  public static JpaEvenIndex create() {
    JpaEvenIndex idx = new JpaEvenIndex();
    return idx;
  }

  public double getIndexValueUsd() {
    return indexValueUsd;
  }

  public double getIndexValueBtc() {
    return indexValueBtc;
  }

  /* needed for db serialization */
  public JpaEvenIndex() {}

  public JpaEvenIndex setTimeStamp(long time) {
    timeStamp = new Date(time);
    return this;
  }

  public Long getId() {
    return id;
  }

  public JpaEvenIndex setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public JpaEvenIndex setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

  public JpaEvenIndex setIndexValueUsd(double indexValueUsd) {
    this.indexValueUsd = indexValueUsd;
    return this;
  }

  public JpaEvenIndex setIndexValueBtc(double indexValueBtc) {
    this.indexValueBtc = indexValueBtc;
    return this;
  }
}
