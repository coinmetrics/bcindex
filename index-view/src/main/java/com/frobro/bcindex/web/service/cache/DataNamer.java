package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.model.api.TimeFrame;

/**
 * Unit test for simple App.
 */
public class DataNamer {
    private static final String DELIMINATOR = ".";

    public static String createName(RequestDto req) {
        return createName(req.index, req.timeFrame, req.currency);
    }

    public static String createName(IndexType index, TimeFrame frame, Currency currency) {
        return createName(index,frame) + DELIMINATOR + currency;
    }

    public static String createName(IndexType index, TimeFrame frame) {
        return index.name() + DELIMINATOR + frame.name();
    }
}
