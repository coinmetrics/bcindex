package com.frobro.bcindex.web;

import com.frobro.bcindex.web.service.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BusinessRulesTest {

    @Test
    public void loadAllBusinessRules() {
        new BusRulesTen();
        new BusRulesTwenty();
        new BusRulesForty();
        new BusRulesEth();
        new BusRulesTotal();
    }
}
