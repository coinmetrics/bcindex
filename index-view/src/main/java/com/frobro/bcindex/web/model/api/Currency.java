package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.DoubleService;

/**
 * Created by rise on 5/12/17.
 */
public enum Currency {
  BTC {
    @Override
    public double format(double raw) {
      return DoubleService.formatBtc(raw);
    }
  },
  USD {
    @Override
    public double format(double raw) {
      return DoubleService.formatUsd(raw);
    }
  };

  abstract public double format(double raw);
}
