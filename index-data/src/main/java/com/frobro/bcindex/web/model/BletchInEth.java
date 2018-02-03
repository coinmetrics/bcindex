package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.rules.BusRulesEth;
import com.frobro.bcindex.web.service.rules.BusinessRules;

/**
 * Created by rise on 7/16/17.
 */
public class BletchInEth extends BletchleyData {
  public static double getDivisor() {
    return StaticValues.DIVISOR_ETHER;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_ETHER;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesEth();
  }
}
