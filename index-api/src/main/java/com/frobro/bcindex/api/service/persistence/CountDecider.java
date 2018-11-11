package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeight;

public class CountDecider {
  private Long counter;
  private Long bletchId;

  public CountDecider(long currCount, JpaWeight jpa) {
    this.counter = currCount;
    setBletchId(jpa);
  }

  private CountDecider setBletchId(JpaWeight jpa) {
    if (jpa != null) {
      this.bletchId = new Long(jpa.getBletchId());
    }
    else {
      this.bletchId = 0L;
    }
    return this;
  }

  public long decide() {
    return counter == 0 ? 0 : bletchId;
  }
}
