package com.frobro.bcindex.web.service;

/**
 * Created by rise on 6/11/17.
 */
public class IndexCalculatorTen extends IndexCalculator {

  public IndexCalculatorTen() {
    BusRulesTen rules = new BusRulesTen();
    this.constant = rules.getConstant();
    this.constantEven = rules.getConstantEven();
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTen();
  }

  @Override
  protected double buildFromSum(double lastSum, double constant, double divisor) {
    return (lastSum + constant)/divisor;
  }

  @Override
  protected String indexName() {
    return "10 INDEX";
  }
}
