package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.model.api.IndexType;

import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 6/13/17.
 */
public class LastPriceCache {
  public double tenPxUsd;
  public double tenPxBtc;
  public double tenEvenPxUsd;
  public double tenEvenPxBtc;
  public double twentyPxUsd;
  public double twentyPxBtc;
  public double twentyEvenPxUsd;
  public double twentyEvenPxBtc;
  public long timeStamp;

  public LastPriceCache setTimeStamp(long time) {
    this.timeStamp = time;
    return this;
  }

  public boolean isMinuteSinceLastUpdate() {
    return (System.currentTimeMillis() - timeStamp) >
        TimeUnit.MINUTES.toMillis(1);
  }

  public double getLast(IndexType index, Currency currency) {
    double price = 0.0;
    switch (index) {
      case ODD:
        if (isUsd(currency)) {
          price = tenPxUsd;
        } else if (isBtc(currency)) {
          price = tenPxBtc;
        }
        break;
      case EVEN:
        if (isUsd(currency)) {
          price = tenEvenPxUsd;
        } else if (isBtc(currency)) {
          price = tenEvenPxBtc;
        }
      break;
      case TWENTY_IDX:
        if (isUsd(currency)) {
          price = twentyPxUsd;
        } else if (isBtc(currency)) {
          price = twentyPxBtc;
        }
        break;
      case TWENTY_EVEN:
        if (isUsd(currency)) {
          price = twentyEvenPxUsd;
        } else if (isBtc(currency)) {
          price = twentyEvenPxBtc;
        }
        break;
      default:
        price = tenPxUsd;
    }
    return price;
  }

  private boolean isUsd(Currency currency) {
    return Currency.USD == currency;
  }

  private boolean isBtc(Currency currency) {
    return Currency.BTC == currency;
  }
  public LastPriceCache setTenPxUsd(double tenPxUsd) {
    this.tenPxUsd = tenPxUsd;
    return this;
  }

  public LastPriceCache setTenPxBtc(double tenPxBtc) {
    this.tenPxBtc = tenPxBtc;
    return this;
  }

  public LastPriceCache setTenEvenPxUsd(double tenEvenPxUsd) {
    this.tenEvenPxUsd = tenEvenPxUsd;
    return this;
  }

  public LastPriceCache setTenEvenPxBtc(double tenEvenPxBtc) {
    this.tenEvenPxBtc = tenEvenPxBtc;
    return this;
  }

  public LastPriceCache setTwentyPxUsd(double twentyPxUsd) {
    this.twentyPxUsd = twentyPxUsd;
    return this;
  }

  public LastPriceCache setTwentyPxBtc(double twentyPxBtc) {
    this.twentyPxBtc = twentyPxBtc;
    return this;
  }

  public LastPriceCache setTwentyEvenPxUsd(double twentyEvenPxUsd) {
    this.twentyEvenPxUsd = twentyEvenPxUsd;
    return this;
  }

  public LastPriceCache setTwentyEvenPxBtc(double twentyEvenPxBtc) {
    this.twentyEvenPxBtc = twentyEvenPxBtc;
    return this;
  }
}