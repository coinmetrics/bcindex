package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;

import java.util.Map;
import java.util.Optional;

/**
 * Created by rise on 4/24/17.
 */
public class MultiplierService {
  private static final BcLog log = BcLog.getLogger(MultiplierService.class);

  private Map<String,Index> lastIndexList;

  public MultiplierService(Map<String,Index> indexes) {
    this.lastIndexList = indexes;
  }
  public MultiplierService updateEvenMktCapIfValid(Optional<Double> multiplier, String ticker) {
    updateMultiplier(multiplier, ticker, Boolean.TRUE);
    return this;
  }

  public MultiplierService updateMarketCapIfValid(Optional<Double> multiplier, String ticker) {
    updateMultiplier(multiplier, ticker, Boolean.FALSE);
    return this;
  }

  public void updateMultiplier(Optional<Double> multiplier, String ticker, boolean isEven) {
    if (multiplier.isPresent()) {
      updateMarketCap(multiplier.get(), lastIndexList.get(ticker), isEven);
    }
    else {
      log.error("cannot calculate market cap for ticker '"
          + ticker + "' no multiplier exists");
    }
  }
  private void updateMarketCap(double multiplier, Index indexForTicker, boolean isEven) {
    double last = indexForTicker.getLast();
    double mktCap = last*multiplier;

    log.debug("updating ticker: " + indexForTicker.getName()
        + " last: " + last + " mkt cap: " + mktCap);

    if (isEven) {
      indexForTicker.setEvenMult(mktCap);
    } else {
      indexForTicker.setMktCap(mktCap);
    }
  }
}
