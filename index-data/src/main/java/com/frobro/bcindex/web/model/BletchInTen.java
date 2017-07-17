package com.frobro.bcindex.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.BusRulesTen;
import com.frobro.bcindex.web.service.BusinessRules;
import com.frobro.bcindex.web.service.CurrPairJson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by rise on 7/13/17.
 */
public class BletchInTen extends BletchleyData {

  public static double getConstant() {
    return StaticValues.CONSTANT_TEN;
  }

  public static double getConstantEven() {
    return StaticValues.CONSTANT_EVEN_TEN;
  }

  public static double getDivisor() {
    return StaticValues.DIVISOR_TEN;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_TEN;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTen();
  }

  @Override
  protected Map<String,Index> populateTickers(String json, Set<String> indexes) throws IOException {
    Map<String, Index> tickers = new TreeMap<>();
    JsonNode root = getRoot(json);

    root.fields().forEachRemaining(node -> {

      double price = node.getValue().get(CurrPairJson.LAST_KEY).asDouble();

      String indexName = node.getKey();
      if (indexes.contains(indexName)) {
        addTicker(tickers, indexName, price);
      }
    });
    return tickers;
  }
}
