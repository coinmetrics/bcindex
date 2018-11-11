package com.frobro.bcindex.web.service.query;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CsvDate {
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public String dateToLine(long t) {
    LocalDateTime date =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.of("Z"));
    return date.format(formatter) + "," + String.valueOf(t) + ",";
  }
}
