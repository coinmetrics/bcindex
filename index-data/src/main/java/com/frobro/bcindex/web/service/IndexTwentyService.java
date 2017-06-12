package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.BletchleyData;

/**
 * Created by rise on 6/11/17.
 */
public class IndexTwentyService {
  private static final BcLog log = BcLog.getLogger(IndexTwentyService.class);
  private final static String COIN_CAP_ENDPOINT = "http://www.coincap.io/global";

  private BletchleyData lastData;

  public void update() {
    log.debug("updating 20 indices");

    lastData = new BletchleyData();

//    String response = makeApiCall();
  }

}
