package com.frobro.bcindex.web.service.apis;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.rules.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Nomics Integration test
 */
public class ApiIntegrationTest {

    @Test
    public void testNomicsParsing() throws Exception {
        // given
        Set<String> coins = getCoins();
        NomicsApi nomics = new NomicsApi();
        nomics.batchCoins(coins);

        // when
        // this will make an external api call to nomics
        Map<String, Index> result = nomics.callBatchedData();

        // then
        assertEquals(coins, result.keySet());
    }

    private Set<String> getCoins() {
        BusRulesTotal total = new BusRulesTotal();
        BusRulesApp sect = new BusRulesApp();
        BusRulesPlatform plat = new BusRulesPlatform();
        BusRulesCurrency curr = new BusRulesCurrency();

        Set<String> allCoins = new HashSet<>();

        allCoins.addAll(total.getIndexes());
        allCoins.addAll(sect.getIndexes());
        allCoins.addAll(plat.getIndexes());
        allCoins.addAll(curr.getIndexes());
        return allCoins;
    }
}
