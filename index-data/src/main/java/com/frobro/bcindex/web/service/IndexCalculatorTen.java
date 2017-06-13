package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.service.persistence.IndexDbDto;

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

  @Override
  protected IndexDbDto newDbDto(double indexPrice, double usdPerBtc, long time) {
    IndexDbDto dto = new IndexDbDto();
    dto.indexValueBtc = indexPrice;
    dto.indexValueUsd = toUsd(indexPrice, usdPerBtc);
    dto.timeStamp = time;
    return dto;
  }

  private double toUsd(double indexPrice, double usdPerBtc) {
    return indexPrice*usdPerBtc;
  }
}
