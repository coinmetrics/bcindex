package com.frobro.bcindex.api.model;

import com.frobro.bcindex.core.model.IndexName;

//TODO: consolidate with index-view WeightDto
public class IndexDto {
  private IndexName index;

  public void setIndex(IndexName name) {
    this.index = name;
  }

  public IndexName getIndex() {
    return index;
  }
}
