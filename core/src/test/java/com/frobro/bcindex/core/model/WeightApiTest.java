package com.frobro.bcindex.core.model;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class WeightApiTest {

  @Test
  public void test() {
    WeightApi api = new WeightApi();
    assertTrue(api.amSecure());
  }
}
