package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.service.MultiplierService;

import java.util.Map;
import java.util.Optional;

abstract class HasEvenIndexRules extends BusinessRules {
  private static final BcLog LOG = BcLog.getLogger(HasEvenIndexRules.class);

  @Override
  public BletchleyData calculateMarketCap(BletchleyData data) {
    super.calculateMarketCap(data);
    LOG.debug("calculating even market cap for " + indexName());

    Map<String,Index> indexList = data.getLastIndexes();
    MultiplierService multService = new MultiplierService(indexList);

    indexList.keySet().stream().forEach(ticker -> {

      Optional<Double> evenMultipler = getMultiplierEven(ticker);
      multService.updateEvenMktCapIfValid(evenMultipler, ticker);
    });
    return data;
  }
}
