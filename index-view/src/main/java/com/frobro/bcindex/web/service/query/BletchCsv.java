package com.frobro.bcindex.web.service.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static com.frobro.bcindex.web.service.query.CsvTimeQuery.*;

class BletchCsv implements Populatable {
  private CsvDate csvDate = new CsvDate();
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

  void addHeader(String header) {
    lines.push(header);
  }

  void prependHeader(String header) {
    lines.push(header);
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
    addToCurrentLine(csvDate.dateToLine(t));
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
