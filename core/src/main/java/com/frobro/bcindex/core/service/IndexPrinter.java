package com.frobro.bcindex.core.service;

import com.frobro.bcindex.core.model.IndexName;

public class IndexPrinter {
  public static void printAllIndices() {
    for (IndexName indexName : IndexName.values()) {
      System.out.println(indexName);
    }
  }
}
