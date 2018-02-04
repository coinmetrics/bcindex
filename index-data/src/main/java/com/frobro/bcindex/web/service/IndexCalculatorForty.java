package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.rules.BusRulesForty;

public class IndexCalculatorForty extends IndexCalculator {

    public IndexCalculatorForty() {
        super(new BusRulesForty());
    }

    @Override
    protected double buildFromSum(double lastSum, double constant, double divisor) {
        return lastSum/divisor;
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
