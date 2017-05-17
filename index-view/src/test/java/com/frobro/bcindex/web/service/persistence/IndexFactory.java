package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;

import java.util.Date;
import java.util.Random;

/**
 * Created by rise on 5/13/17.
 */
public class IndexFactory {
  public static JpaIndex getNewOdd() {
    Random generator = new Random();
    return JpaIndex.create()
        .setTimeStamp(new Date())
        .setIndexValueBtc(generator.nextDouble())
        .setIndexValueUsd(generator.nextDouble());
  }

  public static JpaEvenIndex getNewEven() {
    return getNewEven(System.currentTimeMillis());
  }

  public static JpaEvenIndex getNewEven(long nowInMillis) {
    Random generator = new Random();
    return JpaEvenIndex.create()
        .setTimeStamp(new Date(nowInMillis))
        .setIndexValueBtc(generator.nextDouble())
        .setIndexValueUsd(generator.nextDouble());
  }

}
