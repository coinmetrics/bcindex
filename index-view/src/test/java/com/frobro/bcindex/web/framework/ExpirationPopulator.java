package com.frobro.bcindex.web.framework;

import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.cache.Expiration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpirationPopulator {

    public static Map<IndexType,List<Expiration>> createExpirations(long timeMillis) {
        Map<IndexType,List<Expiration>> map = new HashMap<>();

        for (IndexType index : IndexType.values()) {
            map.put(index,createExpirations(index, timeMillis));
        }
        return map;
    }

    public static List<Expiration> createExpirations(IndexType index,
                                                     long startTimeMillis) {

        List<Expiration> expList = new ArrayList<>(TimeFrame.values().length);

        for (TimeFrame frame : TimeFrame.values()) {
            Expiration expire = new Expiration(index, frame);
            expire.updateLastTime(startTimeMillis);
            expList.add(expire);
        }
        return expList;
    }
}
