package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.rules.BusRulesTwenty;
import com.frobro.bcindex.web.service.rules.BusinessRules;

/**
 * Created by rise on 7/13/17.
 */
public class BletchInTwenty extends BletchleyData {

  public static double getDivisor() {
    return StaticValues.DIVISOR_20;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_20;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTwenty();
  }
}
