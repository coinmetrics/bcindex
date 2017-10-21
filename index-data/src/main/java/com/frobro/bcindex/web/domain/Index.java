package com.frobro.bcindex.web.domain;

import com.frobro.bcindex.web.bclog.BcLog;

/**
 * Created by rise on 3/23/17.
 */
public class Index implements Comparable<Index> {

  private static final BcLog log = BcLog.getLogger(Index.class);
  private static final double UNINITIALIZED = -99999;
  private String name;
  private double last;
  private double mktCap = UNINITIALIZED;
  private double evenMult = UNINITIALIZED;

  @Override
  public int compareTo(Index other) {
    int result = 0;
    if (this.getMktCap() > other.getMktCap()) {
      result = 1;
    } else if (this.getMktCap() < other.getMktCap()) {
      result = -1;
    }
    return result;
  }

  public Index setName(String name) {
    this.name = name;
    return this;
  }

  public Index setEvenMult(double mult) {
    this.evenMult = mult;
    return this;
  }

  public Index setMktCap(double cap) {
    this.mktCap = cap;
    return this;
  }

  public Index setLast(double last) {
    // hack for issue #39
    if ("XLM".equalsIgnoreCase(name)) {
      this.last = 2.4;
    }
    else {
      this.last = last;
    }
    return this;
  }

  public double getEvenMult() {
    return evenMult;
  }

  public double getMktCap() {
    return mktCap;
  }

  public double getLast() {
    return last;
  }

  public String getName() {
    return name;
  }

  public boolean isMktCapValid() {
    return this.mktCap != UNINITIALIZED;
  }

  @Override
  public String toString() {
    return "Index{" +
        "name='" + name + '\'' +
        ", last=" + last +
        ", mktCap=" + mktCap +
        '}';
  }
}
