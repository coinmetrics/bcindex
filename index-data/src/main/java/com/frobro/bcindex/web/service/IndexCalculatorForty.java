package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

public class IndexCalculatorForty extends IndexCalculator {
    @Override
    protected double buildFromSum(double lastSum, double constant, double divisor) {
        return lastSum/divisor;
    }

    @Override
    protected BusinessRules newBusRules() {
        return new BusRulesEth();
    }

    @Override
    protected String indexName() {
        return "FORTY INDEX";
    }

    @Override
    protected double getDivisor() {
        return StaticValues.DIVISOR_40;
    }

    @Override
    protected double getDivisorEven() {
        return StaticValues.DIVISOR_EVEN_40;
    }

    @Override
    protected IndexDbDto newDbDto(double indexPrice, double usdPerBtc, long time) {
        IndexDbDto dto = new IndexDbDto();
        dto.indexValueBtc = toBtc(indexPrice, usdPerBtc);
        dto.indexValueUsd = indexPrice;
        dto.timeStamp = time;
        return dto;
    }

  private double toBtc(double px, double usdBtc) {
    return px/usdBtc;
  }
}
