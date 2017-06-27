package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.service.util.BletchFiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rise on 6/27/17.
 */
public class FileDataSaver {
  private static final List<String> filesTen = Arrays.asList("db_files/10_index.csv");
  private static final List<String> filesTwenty = new ArrayList<>(8);

  private static final String DELIM = ",";
  private static final String FORMAT = "MM/dd/yy HH:mm";

  private static final long TIME_INC = 1440; // days (1440 min/day)
  private static final int BTC_POS = 1;
  private static final int USD_POS = 2;
  private static final int EVEN_BTC_POS = 3;
  private static final int EVEN_USD_POS = 4;
  private static final int TIME_POS = 0;

  private IndexRepo indexRepo;
  private EvenIdxRepo evenIdxRepo;
  private TwentyRepo twentyRepo;
  private TwentyEvenRepo twentyEvenRepo;

  public FileDataSaver(IndexRepo ir, EvenIdxRepo eir,
                       TwentyRepo tr, TwentyEvenRepo ter) {
    this.indexRepo = ir;
    this.evenIdxRepo = eir;
    this.twentyRepo = tr;
    this.twentyEvenRepo = ter;
  }

  public void saveData() {
    for (String file : filesTen) {
      saveDataTen(file);
    }

    for (String file : filesTwenty) {
      saveDataTwenty(file);
    }
  }

  private void saveDataTwenty(String fileName) {
    System.out.println("getting lines for file: " + fileName);

    List<String> lines = BletchFiles.linesToList(fileName);

    List<JpaIdxTwenty> tenList = new ArrayList<>(lines.size());
    List<JpaTwentyEven> evenList = new ArrayList<>(lines.size());

    long bid = 1;
    // skip the header line at position zero
    for (int i=1; i<lines.size(); i++) {
      String line = lines.get(i);

      JpaIdxTwenty twenty = new JpaIdxTwenty();
      JpaTwentyEven evenTwenty = new JpaTwentyEven();

      String[] data =  line.split(DELIM);
      setData(data, twenty, EVEN_BTC_POS, EVEN_USD_POS, bid);
      setData(data, evenTwenty, EVEN_BTC_POS, EVEN_USD_POS, bid);

      tenList.add(twenty);
      evenList.add(evenTwenty);

      bid = updateBid(bid);
    }

    this.twentyRepo.save(tenList);
    this.twentyEvenRepo.save(evenList);
  }

  private void saveDataTen(String fileName) {
    List<String> lines = BletchFiles.linesToList(fileName);

    List<JpaIndexTen> tenList = new ArrayList<>(lines.size());
    List<JpaEvenIndex> evenList = new ArrayList<>(lines.size());

    long bid = 1;
    // skip the header line at position zero
    for (int i=1; i<lines.size(); i++) {
      String line = lines.get(i);

      JpaIndexTen ten = new JpaIndexTen();
      JpaEvenIndex evenTen = new JpaEvenIndex();

      String[] data =  line.split(DELIM);
      setData(data, ten, BTC_POS, USD_POS, bid);
      setData(data, evenTen, BTC_POS, USD_POS, bid);

      tenList.add(ten);
      evenList.add(evenTen);

      bid = updateBid(bid);
    }

    this.indexRepo.save(tenList);
    this.evenIdxRepo.save(evenList);
  }

  private long updateBid(long bid) {
    if (bid == 1) {
      bid = 0;
    }

    return bid + TIME_INC;
  }
  private void setData(String[] data, JpaIndex idx,
                       int btcpos, int usdpos, long bid) {
    idx.setBletchId(bid);
    idx.setIndexValueBtc(Double.parseDouble(data[btcpos]));
    idx.setIndexValueUsd(Double.parseDouble(data[usdpos]));
    idx.setTimeStamp(parseDate(data[TIME_POS]));
  }

  private long parseDate(String dateStr) {
    long date = -999990099;
    try {

      SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT);
      Date inputDate = dateFormat.parse(dateStr);
      date = inputDate.getTime();

    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }
    return date;
  }
}
