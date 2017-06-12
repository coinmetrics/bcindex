package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;

/**
 * Created by rise on 6/11/17.
 */
public class IndexCalculatorTwenty extends IndexCalculator {
  private static final BcLog log = BcLog.getLogger(IndexCalculatorTwenty.class);

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTwenty();
  }

  @Override
  protected double buildFromSum(double lastSum, double constant, double divisor) {
    return lastSum/divisor;
  }

  @Override
  protected String indexName() {
    return "20 INDEX";
  }
}
