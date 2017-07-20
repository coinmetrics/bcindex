package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.model.BletchInTen;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

/**
 * Created by rise on 6/11/17.
 */
public class IndexCalculatorTen extends IndexCalculator {

  public IndexCalculatorTen() {
    this.constant = BletchInTen.getConstant();
    this.constantEven = BletchInTen.getConstantEven();
  }

  @Override
  protected BusinessRules newBusRules() {
    return new BusRulesTen();
  }

  @Override
  protected double getDivisor() {
    return BletchInTen.getDivisor();
  }

  @Override
  protected double getDivisorEven() {
    return BletchInTen.getDivisorEven();
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
    dto.indexValueBtc = toUsd(indexPrice, usdPerBtc);
    dto.indexValueUsd = indexPrice; 
    dto.timeStamp = time;
    return dto;
  }

  private double toUsd(double indexPrice, double usdPerBtc) {
    return indexPrice/usdPerBtc;
  }
}
