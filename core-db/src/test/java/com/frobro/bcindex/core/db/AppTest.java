package com.frobro.bcindex.core.db;

import com.frobro.bcindex.core.db.service.BletchDate;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest {

  @Test
  public void testApp() {
    long millisTime = System.currentTimeMillis();
    long trun = BletchDate.roundWeek(millisTime);

    System.out.println(BletchDate.toDate(millisTime));
    System.out.println(BletchDate.toDate(trun));

  }

}
