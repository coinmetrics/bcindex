package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.rules.BusRulesForty;
import com.frobro.bcindex.web.service.rules.BusRulesTen;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BletchleyDataTest {

  @Test
  public void testErrorIfMissingCoin() {
    /*
      BTC, ETH, LTC, ... - RULES
      BTC, <missing>, LTC, ... - RECEIVED
     */
    BletchleyData indexData = new BletchleyData(new BusRulesTen());

    Map<String,Index> coinData = toIndex(indexData.getMembers());
    String missingCoin = "ETH";
    coinData.remove(missingCoin);

    // when
    Set<String> missedCoins = indexData.filterAndSet(coinData);
    // then
    assertEquals(1, missedCoins.size());
    assertTrue(missedCoins.contains(missingCoin));
  }

  private Map<String,Index> toIndex(Set<String> coins) {
    Map<String,Index> map = new HashMap<>();
    for (String name :  coins) {
      map.put(name, new Index().setName(name));
    }
    return map;
  }
}
