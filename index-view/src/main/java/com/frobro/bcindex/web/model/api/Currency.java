package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.web.service.DoubleFormatter;

/**
 * Created by rise on 5/12/17.
 */
public enum Currency {
  BTC {
    @Override
    public double format(double raw) {
      return DoubleFormatter.formatBtc(raw);
    }
  },
  USD {
    @Override
    public double format(double raw) {
      return DoubleFormatter.formatUsd(raw);
    }
  };

  abstract public double format(double raw);
}
