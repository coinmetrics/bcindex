package com.frobro.bcindex.web.model.api.weight;

import com.frobro.bcindex.core.db.model.IndexName;

import java.util.List;

public class WeightListDto {
  private List<IndexName> indexList;

  public void setIndexList(List<IndexName> list) {
    this.indexList = list;
  }

  public List<IndexName> getIndexList() {
    return indexList;
  }
}
