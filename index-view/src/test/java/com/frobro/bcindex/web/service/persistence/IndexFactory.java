package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rise on 5/13/17.
 */
public class IndexFactory {
  public static JpaIndex getNewOdd() {
    return JpaIndex.create()
        .setTimeStamp(new Date())
        .setIndexValueBtc(nextDouble())
        .setIndexValueUsd(nextDouble());
  }

  private static double nextDouble() {
    int min = 10;
    int max = 1000;
    return ThreadLocalRandom.current().nextInt(min, max + 1);
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
