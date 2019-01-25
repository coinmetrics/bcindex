package com.frobro.bcindex.core.db.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "max_time")
public class JpaMaxTime {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private String indexName;
  private long bletchIdTen;
  private long timeStampTen;
  private double indexValueUsdTen;
  private double indexValueBtcTen;

}
