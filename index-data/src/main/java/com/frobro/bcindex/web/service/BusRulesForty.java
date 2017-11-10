package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.Ticker;

import java.util.HashMap;
import java.util.Map;

public class BusRulesForty extends BusinessRules {
    private static final BcLog log = BcLog.getLogger(BusRulesForty.class);
    protected static Map<String,Ticker> tickers;

    public BusRulesForty() {
        if (shouldPopulate(tickers)) {
            populate();
            logMultipliers();
        }
    }

    private void populate() {
        tickers = new HashMap<>();
        populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE_40);
    }

    private void logMultipliers() {
        log.debug("10 index multipliers");
        logValues(tickers);
    }

    @Override
    protected Map<String,Ticker> getTickers() {
        return tickers;
    }

    @Override
    public double getDivisor() {
        return StaticValues.DIVISOR_40;
    }

    @Override
    public double getDivisorEven() {
        return StaticValues.DIVISOR_EVEN_40;
    }
}

