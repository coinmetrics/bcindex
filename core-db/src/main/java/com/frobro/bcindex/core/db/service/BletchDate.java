package com.frobro.bcindex.core.db.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 6/17/17.
 */
public class BletchDate {
  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final String TIME_ZONE = "America/Chicago";

  public static String toDate(long time) {
    LocalDateTime date = LocalDateTime.
  }

  public static Date toDate(String dateStr) {
    try {

      return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);

    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }
  }

  public static long weeksToMillis(long wks) {
    return daysToMillis(wks * 7);
  }

  public static long monthsToMillis(long months) {
    return daysToMillis(months * 31);
  }

  private static long daysToMillis(long days) {
    return TimeUnit.DAYS.toMillis(days);
  }

  public static long yearsToMillis(long yrs) {
    return daysToMillis(yrs*365);
  }
}
