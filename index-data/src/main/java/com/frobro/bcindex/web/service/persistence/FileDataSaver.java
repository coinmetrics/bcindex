package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.core.db.service.files.BletchFiles;

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
  private static final Integer EXPORT = 1;
  private static final Integer DB_DUMP = 2;
  private static final Integer DB_DUMP_EVEN = 3;
  private static final List<String> filesTen = Arrays.asList("db_files/10_index_full_history.csv");
  private static final List<String> filesTwenty = Arrays.asList("db_files/20_index_full_history.csv");
  private static final List<String> filesEth = Arrays.asList("db_files/eth_history.csv");

  private static final String DELIM = ",";
  private static final String FORMAT = "MM/dd/yy HH:mm";

  private static final long TIME_INC = 120; // 2 hours
  private static final int BTC_POS = 1;
  private static final int USD_POS = 2;
  private static final int EVEN_BTC_POS = 3;
  private static final int EVEN_USD_POS = 4;
  private static final int TIME_POS = 0;

  private IndexRepo indexRepo;
  private EvenIdxRepo evenIdxRepo;
  private TwentyRepo twentyRepo;
  private TwentyEvenRepo twentyEvenRepo;
  private EthRepo ethRepo;
  private EthEvenRepo ethEvenRepo;
  private PrimeRepo primeRepo;

  public FileDataSaver(IndexRepo ir, EvenIdxRepo eir,
                       TwentyRepo tr, TwentyEvenRepo ter,
                       EthRepo ethRepo, EthEvenRepo ethEvenRepo) {
    this.indexRepo = ir;
    this.evenIdxRepo = eir;
    this.twentyRepo = tr;
    this.twentyEvenRepo = ter;
    this.ethRepo = ethRepo;
    this.ethEvenRepo = ethEvenRepo;
    primeRepo = PrimeRepo.getRepo(ir,eir,tr,ter,ethRepo,ethEvenRepo);
  }

  public void saveData() {
//    saveDataEth("db_files/eth_history.csv", EXPORT);
//    saveDataEth("db_files/eth_dump_7_18_2017.csv", DB_DUMP);
    saveDataEthEven("/home/rise/Linux_files/repos/bcindex/utils/db_data/eth_even_prod.csv");

    for (String file : filesTen) {
      saveDataTen(file);
    }
//
//    for (String file : filesTwenty) {
//      saveDataTwenty(file);
//    }
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

      primeRepo.saveTen(ten);
      primeRepo.saveTenEven(evenTen);

      bid = updateBid(bid);
    }
  }

  private void saveDataEthEven(String fileName) {
    List<String> lines = BletchFiles.fileSystemToList(fileName);

    for (int i=1; i<lines.size(); i++) {
      String line = lines.get(i);

      JpaIdxEthEven idx = new JpaIdxEthEven();
      String[] data = line.split(DELIM);

      long time = Long.parseLong(data[4]);
      // btc=2, usd=4, time=4
      setData(data, idx, 2, 3, time);

      primeRepo.saveEthEven(idx);
    }
  }

  private void saveDataEth(String fileName, Integer type) {
    List<String> lines = BletchFiles.fileSystemToList(fileName);

    List<JpaIdxEth> tenList = new ArrayList<>(lines.size());
    List<JpaIdxEthEven> evenList = new ArrayList<>(lines.size());

    // skip the header line at position zero
    for (int i=1; i<lines.size(); i++) {
      String line = lines.get(i);

      JpaIdxEth ten = new JpaIdxEth();
      JpaIdxEthEven evenTen = new JpaIdxEthEven();

      String[] data =  line.split(DELIM);

      if (DB_DUMP.equals(type)) {
        long time = Long.parseLong(data[4]);
        // btc=2, usd=4, time=4
        setData(data, ten, 2, 3, time);
      }
      else if (DB_DUMP_EVEN.equals(type)) {
        long time = Long.parseLong(data[4]);
        setData(data, evenTen, 2, 3, time);
      }
      else {
        setData(data, ten, BTC_POS, USD_POS);
        setData(data, evenTen, EVEN_BTC_POS, EVEN_USD_POS);
      }

      if (DB_DUMP.equals(type)) {
        primeRepo.saveEth(ten);
      }
      else if (DB_DUMP_EVEN.equals(type)) {
        primeRepo.saveEthEven(evenTen);
      } else {
        primeRepo.saveEth(ten);
        primeRepo.saveEthEven(evenTen);
      }
    }
  }

  private long updateBid(long bid) {
    if (bid == 1) {
      bid = 0;
    }

    return bid + TIME_INC;
  }
  private void setData(String[] data, JpaIndex idx,
                       int btcpos, int usdpos) {
    setData(data, idx, btcpos, usdpos,
            parseDate(data[TIME_POS]));
  }

  private void setData(String[] data, JpaIndex idx,
                       int btcpos, int usdpos,
                       long time) {
    idx.setIndexValueBtc(Double.parseDouble(data[btcpos]));
    idx.setIndexValueUsd(Double.parseDouble(data[usdpos]));
    idx.setTimeStamp(time);
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
