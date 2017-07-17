package com.frobro.bcindex.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.BusRulesTen;
import com.frobro.bcindex.web.service.BusRulesTwenty;
import com.frobro.bcindex.web.service.BusinessRules;
import com.frobro.bcindex.web.service.CurrPairJson;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

  @Override
  protected Map<String,Index> populateTickers(String json,
                                       Set<String> indexes) throws IOException {
    Map<String, Index> tickers = new TreeMap<>();
    ArrayNode root = (ArrayNode) getRoot(json);

    root.elements().forEachRemaining(node -> {
      JsonNode pxNd = node.get(CurrPairJson.PRICE_KEY_20);
      double price = pxNd.asDouble();

      JsonNode symbolNd = node.get(CurrPairJson.NAME_KEY_20);
      String symbol = symbolNd.asText();

      if (indexes.contains(symbol)) {
        addTicker(tickers, symbol, price);
      }
    });
    return tickers;
  }
}
