package com.frobro.bcindex.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by rise on 6/17/17.
 */
public class BletchDate {
  public static final long MIN_IN_HOUR = 60;
  public static final long MIN_IN_DAY = MIN_IN_HOUR * 24;
  public static final long MIN_IN_WEEK = MIN_IN_DAY * 7;
  public static final long MIN_IN_MONTH = MIN_IN_DAY * 31;
  public static final long MIN_IN_YEAR = MIN_IN_DAY * 365;
  public static final long MIN_IN_QUARTER = MIN_IN_YEAR / 4;
  public static final long MIN_IN_4_HOURS = MIN_IN_HOUR * 4;
  public static final long MIN_IN_6_HOURS = MIN_IN_HOUR * 6;
  public static final long MIN_IN_12_HOURS = MIN_IN_HOUR * 12;

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String TIME_ZONE = "America/Chicago";

  public static String toDate(long time) {
    DateTimeFormatter frmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
    LocalDateTime date = toLocalDateTime(time);
    return date.format(frmt);
  }

  public static long toEpochMilli(String date) {
    LocalDateTime time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    return time.atZone(ZoneId.of(TIME_ZONE)).toInstant().toEpochMilli();
  }

  private static LocalDateTime toLocalDateTime(long time) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(time),
        ZoneId.of(TIME_ZONE));
  }

  public static Date toDate(String dateStr) {
    try {

      return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);

    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }
  }

  public static long getZeroUtcToday() {
    return getZeroUtc(Clock.systemUTC(),0);
  }

  public static long getZeroUtcToday(Clock clock) {
    return getZeroUtc(clock, 0);
  }

  public static long getZeroUtcTomorrow() {
    return getZeroUtc(Clock.systemUTC(), 1);
  }

  public static long getZeroUtcTomorrow(Clock clock) {
    return getZeroUtc(clock,1);
  }

  // allow clock to be swapped for testing
  public static long getZeroUtc(Clock clock, long dayOffset) {
    LocalTime midnight = LocalTime.MIDNIGHT;
    LocalDate today = LocalDate.now(clock);
    LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

    todayMidnight = todayMidnight.plusDays(dayOffset);

    ZoneId id = ZoneId.of("UTC");
    ZonedDateTime zdt = todayMidnight.atZone(id);
    return SECONDS.toMillis(zdt.toEpochSecond());
  }

  public static long weeksToMillis(long wks) {
    return daysToMillis(wks * 7);
  }

  public static long monthsToMillis(long months) {
    return daysToMillis(months * 31);
  }

  public static long daysToMillis(long days) {
    return TimeUnit.DAYS.toMillis(days);
  }

  public static long yearsToMillis(long yrs) {
    return daysToMillis(yrs * 365);
  }

  public static long roundMinute(long raw) {
    long timeMin = TimeUnit.MILLISECONDS.toMinutes(raw);
    return TimeUnit.MINUTES.toMillis(timeMin);
  }

  public static long roundHour(long raw) {
    long hourTime = TimeUnit.MILLISECONDS.toHours(raw);
    return TimeUnit.HOURS.toMillis(hourTime);
  }

  public static long roundDay(long raw) {
    long dayTime = TimeUnit.MILLISECONDS.toDays(raw);
    return TimeUnit.DAYS.toMillis(dayTime);
  }

  public static long roundWeek(long raw) {
    long timeMin = TimeUnit.MILLISECONDS.toMinutes(raw);
    long truncated = timeMin / MIN_IN_WEEK;
    long millis = TimeUnit.MINUTES.toMillis(truncated*MIN_IN_WEEK);
    return millis;
  }

  public static long roundQuarter(long raw) {
    long timeMin = TimeUnit.MILLISECONDS.toMinutes(raw);
    long truncated = timeMin/MIN_IN_QUARTER;
    long millis = TimeUnit.MINUTES.toMillis(truncated*MIN_IN_QUARTER);
    return millis;
  }

  public static String round(String format, String raw) {
    return round(format, toDate(raw).getTime());
  }

  public static String round(String format, long raw) {
    DateTimeFormatter frmt = DateTimeFormatter.ofPattern(format);
    LocalDateTime date = toLocalDateTime(raw);

    return date.format(frmt);
  }
}