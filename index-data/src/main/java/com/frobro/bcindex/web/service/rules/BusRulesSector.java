package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.core.db.service.files.BletchFiles;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.Ticker;

import java.util.List;
import java.util.Map;

public abstract class BusRulesSector extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesApp.class);

  protected void populateValuesFromFile(Map<String,Ticker> map, String fileName) {
    List<String> lines = BletchFiles.linesToList(fileName);
    lines.stream().forEach(line -> {
      String[] vals = line.split(DELIMINATOR);

      String name = vals[NAME_POS];

      Ticker ticker = new Ticker(name);

      String multStr = vals[MULT_POS];
      Double multiplier = Double.parseDouble(multStr);
      ticker.setMultiplier(multiplier);

      map.put(ticker.getName(), ticker);
    });
  }

  public double getConstant() {
    throw new UnsupportedOperationException("no constant for " + indexName());
  }

  public double getConstantEven() {
    throw new UnsupportedOperationException("no constant for " + indexName());
  }

  @Override
  public double getDivisorEven() {
    return -1* Double.MAX_VALUE;
  }

  abstract String getFileName();
}
