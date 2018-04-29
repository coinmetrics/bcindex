package com.frobro.bcindex.api.domain.weight;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class JpaIndexWeight extends JpaWeight {
  private double weightEven;

  public double getWeightEven() {
    return weightEven;
  }

  public void setWeightEven(double weightEven) {
    this.weightEven = weightEven;
  }
}
