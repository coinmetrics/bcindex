package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.core.db.service.files.BletchFiles;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.persistence.FileDataSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by rise on 5/20/17.
 * Only use in dev modes
 */
@RestController
@Profile({"dev","pgres"})
public class SeedController {

  private static final BcLog log = BcLog.getLogger(SeedController.class);
  private TickerService tickerService = new TickerService();
  private FileDataSaver fileDataSaver;
  PrimeRepo repo;

  @Autowired
  public void setRepos(EvenIdxRepo eRepo, IndexRepo oRepo,
                       TwentyRepo twRepo, TwentyEvenRepo teRepo,
                       EthRepo ethRepo, EthEvenRepo eteRepo) {

    repo = PrimeRepo.getRepo(oRepo,eRepo,twRepo,teRepo,ethRepo,eteRepo);
    tickerService.setIndexRepo(oRepo, eRepo, twRepo, teRepo, ethRepo, eteRepo);
    // ETH index not currently supported in file saver
    fileDataSaver = new FileDataSaver(oRepo, eRepo, twRepo, teRepo,ethRepo,eteRepo);
  }

  @PostConstruct
  public void start(){
    log.info("populating the database with mock data ...");
    seed();
//    fileDataSaver.saveData();
  }

  @RequestMapping("/filedata")
  public String seedFromFiles() {
    fileDataSaver.saveData();
    return "done populating data from files";
  }

  @RequestMapping("/newdata")
  public String newData() {
    tickerService.updateTickers();
    return "done getting new data";
  }

  @RequestMapping("/seed")
  public String seed() {
    // pop with real data
    List<JpaIndexTen> oddList = populateDataTen("odd_data.csv");
    int numIterations = oddList.size();

    // pop with fake data
    long time = System.currentTimeMillis();
    for (int i=0; i<numIterations; i++) {
      time  += nextMinute();
      repo.saveTenEven(newEven(time));
      repo.saveTwenty(newTwenty(time));
      repo.saveEvenTwenty(new20Even(time));
      repo.saveEthEven(newEthEven(time));
    }
    return "done seeding";
  }

  private boolean tooManyLines(int size, int maxSize) {
    return size > maxSize;
  }

  private List<JpaIndexTen> populateDataTen(String fileName) {
    final String delim = ",";
    final int btcPos = 1, usdPos = 2, datePos = 3;
    List<String> lines = BletchFiles.linesToList(fileName);
    int numHours = 26;
    int numIterations = (int) TimeUnit.HOURS.toMinutes(numHours);

    int size = tooManyLines(lines.size(), numIterations) ? numIterations : lines.size();

    List<JpaIndexTen> idxList = new ArrayList<>(size);

    long lastDate = 0;
    long diff = 0;
    long time = 0;
    for (int i=size-1; i>=0; i--) {
      String line = lines.get(i);
      String[] vals = line.split(delim);
      time = Long.parseLong(vals[datePos]);

      // if is the most recent time set the time
      // diff so all times will be recent
      if (i == size-1) {
        diff = System.currentTimeMillis() - time;
      }

      JpaIndexTen idx = new JpaIndexTen();
      idx.setId(Long.valueOf(i));
      idx.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
      idx.setIndexValueUsd(Double.parseDouble(vals[usdPos]));

      idx.setTimeStamp(time + diff);
      idxList.add(idx);
      repo.saveTen(idx);

      JpaIdxEth eth = new JpaIdxEth();
      eth.setId(Long.valueOf(i));
      eth.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
      eth.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
      eth.setTimeStamp(BletchDate.toDate(vals[datePos]));
      repo.saveEth(eth);

      if (i == (size-1)) {
        lastDate = BletchDate.toDate(vals[datePos]).getTime();
      }
    }

    // populate again!
    lastDate += TimeUnit.MINUTES.toMillis(1);
    for (int i=1; i<size; i++) {
      String line = lines.get(i);
      String[] vals = line.split(delim);
      JpaIndexTen idx = new JpaIndexTen();
      idx.setId(Long.valueOf(size + i));
      idx.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
      idx.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
      idx.setTimeStamp(lastDate);
      idxList.add(idx);
      repo.saveTen(idx);

      JpaIdxEth eth = new JpaIdxEth();
      eth.setId(Long.valueOf(size+i));
      eth.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
      eth.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
      eth.setTimeStamp(BletchDate.toDate(vals[datePos]));
      repo.saveEth(eth);

      lastDate += TimeUnit.MINUTES.toMillis(1);
    }

    return idxList;
  }

  private JpaIndexTen newOdd(long now) {
    JpaIndexTen idx = new JpaIndexTen();
    populate(idx, now);
    return idx;
  }

  private JpaEvenIndex newEven(long now) {
    JpaEvenIndex idx = new JpaEvenIndex();
    populate(idx, now);
    return idx;
  }

  private JpaIdxTwenty newTwenty(long now) {
    JpaIdxTwenty idx = new JpaIdxTwenty();
    populate(idx, now);
    return idx;
  }

  private JpaTwentyEven new20Even(long now) {
    JpaTwentyEven idx = new JpaTwentyEven();
    populate(idx, now);
    return idx;
  }

  private JpaIdxEthEven newEthEven(long now) {
    JpaIdxEthEven idx = new JpaIdxEthEven();
    populate(idx, now);
    return idx;
  }

  private void populate(JpaIndex idx, long time) {
    double nextDouble = nextDouble();
    idx.setTimeStamp(time)
        .setIndexValueUsd(nextDouble)
        .setIndexValueBtc(nextDouble / 10.0);
  }

  private double nextDouble() {
    int min = 10;
    int max = 1000;
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }
  private long nextMinute() {
    return TimeUnit.MINUTES.toMillis(1);
  }
}
