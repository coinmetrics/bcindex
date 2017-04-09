package com.frobro.bcindex.web.domain;

/**
 * Created by rise on 4/9/17.
 */
public class JpaIndex {
  private double indexValueUsd;
  private double usdPerBtc;

  public double getIndexValueUsd() {
    return indexValueUsd;
  }

  public double getUsdPerBtc() {
    return usdPerBtc;
  }

  public JpaIndex(double indexValueUsd, double usdPerBtc) {
    this.indexValueUsd = indexValueUsd;
    this.usdPerBtc = usdPerBtc;

  }
}
