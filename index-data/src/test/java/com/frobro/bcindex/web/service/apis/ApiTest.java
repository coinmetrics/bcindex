package com.frobro.bcindex.web.service.apis;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.rules.*;
import com.frobro.bcindex.web.testframework.HttpApiMock;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class ApiTest {

    @Test
    public void testNomicsParsing() throws Exception {
        // given
        Set<String> coins = getCoins();
        NomicsApi nomics = new NomicsApi();
        nomics.batchCoins(coins);
        nomics.mock(new HttpApiMock());

        // when
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
