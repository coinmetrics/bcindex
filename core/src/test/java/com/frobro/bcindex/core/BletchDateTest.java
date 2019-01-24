package com.frobro.bcindex.core;

import com.frobro.bcindex.core.service.BletchDate;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BletchDateTest {

  @Test
  public void testUtcDate() {
    long zeroUtcToday = BletchDate.getZeroUtcToday();
    long zeroUtcTomorrow = BletchDate.getZeroUtcTomorrow();

    long now = System.currentTimeMillis();
    assertTrue(zeroUtcToday - now <= 0);
    assertTrue(zeroUtcTomorrow - now >= 0);

    long diffInMillis = zeroUtcTomorrow - zeroUtcToday;
    long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
    assertEquals(24, diffInHours);
  }
}
