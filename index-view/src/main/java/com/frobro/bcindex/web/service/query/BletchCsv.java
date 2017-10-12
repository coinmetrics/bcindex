package com.frobro.bcindex.web.service.query;

import static com.frobro.bcindex.web.service.query.CsvTimeQuery.getBtcCol;
import static com.frobro.bcindex.web.service.query.CsvTimeQuery.getTimeCol;
import static com.frobro.bcindex.web.service.query.CsvTimeQuery.getUsdCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

class BletchCsv implements Populatable {
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private String firstPxUsd;
  private String firstPxBtc;
  private Long firstEpochTime;
  private String lastPxUsd;
  private String lastPxBtc;
  private Long lastEpochTime;
  LinkedList<String> lines = new LinkedList<>();
  String currLine = "";

  BletchCsv newline() {
    lines.add(currLine);
    currLine = "";
    return this;
  }

  List<String> getLines() {
    return lines;
  }

  private boolean not(boolean b) {
    return !b;
  }

  void addHeader(String header) {
    String firstLine = dateToLine(firstEpochTime) + firstPxBtc + firstPxUsd;
    addFirstLineIfAbsent(firstLine);

    lines.push(header);

    String lastLine = dateToLine(lastEpochTime) + lastPxBtc + lastPxUsd;
    addLastLineIfAbsent(lastLine);
  }

  void prependHeader(String header) {
    lines.push(header);
  }

  private void addFirstLineIfAbsent(String firstLine) {
    if (not(firstLine.equals(lines.get(0)))) {
      lines.push(firstLine);
    }
  }

  private void addLastLineIfAbsent(String lastLine) {
    if (not(lastLine.equals(lines.get(lines.size()-1)))) {
      lines.addLast(lastLine);
    }
  }

  private void addToCurrentLine(String s) {
    currLine = currLine.concat(s);
  }

  BletchCsv usdPrice(String px) {
    addToCurrentLine(px);
    return this;
  }

  BletchCsv btcPrice(String px) {
    addToCurrentLine(px);
    return this;
  }

  BletchCsv epochTime(long t) {
    addToCurrentLine(dateToLine(t));
    return this;
  }

  private String dateToLine(long t) {
    LocalDateTime date =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.of("America/Chicago"));
    return date.format(formatter) + "," + String.valueOf(t) + ",";
  }

  BletchCsv firstPxUsd(String px) {
    if (this.firstPxUsd == null) {
      firstPxUsd = px;
    }
    return this;
  }

  BletchCsv firstPxBt(String px) {
    if (this.firstPxBtc == null) {
      firstPxBtc = px;
    }
    return this;
  }

  BletchCsv firstEpochTime(long t) {
    if (this.firstEpochTime == null) {
      firstEpochTime = t;
    }
    return this;
  }

  BletchCsv lastPxUsd(String px) {
    if (lastPxUsd == null) {
      lastPxUsd = px;
    }
    return this;
  }

  BletchCsv lastPxBtc(String px) {
    if (lastPxBtc == null) {
      lastPxBtc = px;
    }
    return this;
  }

  BletchCsv lastEpochTime(long t) {
    if (lastEpochTime == null) {
      lastEpochTime = t;
    }
    return this;
  }

  @Override
  public BletchCsv populate(ResultSet result, int size) throws SQLException {

    for (int i=1; i<=size; i++) {
      long epochTime = result.getLong(getTimeCol(i));
      epochTime(epochTime);

      btcPrice(result.getString(getBtcCol(i)) + ",");
      usdPrice(result.getString(getUsdCol(i)));
      newline();
    }
    return this;
  }
}
