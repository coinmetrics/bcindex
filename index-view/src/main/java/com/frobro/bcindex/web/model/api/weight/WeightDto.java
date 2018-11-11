package com.frobro.bcindex.web.model.api.weight;

import com.frobro.bcindex.core.model.IndexName;

public class WeightDto {
  private IndexName index;

  public void setIndex(IndexName name) {
    this.index = name;
  }

  public IndexName getIndex() {
    return index;
  }
}
