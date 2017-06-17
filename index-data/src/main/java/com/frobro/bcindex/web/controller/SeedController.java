package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.util.BletchFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  private EvenIdxRepo evenRepo;
  private IndexRepo oddRepo;
  private TwentyRepo twentyRepo;
  private TwentyEvenRepo twentyEvenRepo;

  @Autowired
  public void setRepos(EvenIdxRepo eRepo, IndexRepo oRepo,
                       TwentyRepo twRepo, TwentyEvenRepo teRepo) {
    this.evenRepo = eRepo;
    this.oddRepo = oRepo;
    this.twentyRepo = twRepo;
    this.twentyEvenRepo = teRepo;
    tickerService.setIndexRepo(oRepo, eRepo, twRepo, teRepo);
  }

  @PostConstruct
  public void start(){
    log.info("populating the database with mock data ...");
    seed();
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
    List<JpaEvenIndex> evenList = new ArrayList<>(numIterations);
    List<JpaIdxTwenty> list20 = new ArrayList<>(numIterations);
    List<JpaTwentyEven> listEven20 = new ArrayList<>(numIterations);
    long time = System.currentTimeMillis();
    for (int i=0; i<numIterations; i++) {
      time  += nextMinute();
      evenList.add(newEven(time));
      list20.add(newTwenty(time));
      listEven20.add(new20Even(time));
    }

    // save data
    evenRepo.save(evenList);
    oddRepo.save(oddList);
    twentyRepo.save(list20);
    twentyEvenRepo.save(listEven20);

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

    for (int i=1; i<size; i++) {
      String line = lines.get(i);
      String[] vals = line.split(delim);
      JpaIndexTen idx = new JpaIndexTen();
      idx.setId(Long.valueOf(i));
      idx.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
      idx.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
      idx.setTimeStamp(BletchDate.toDate(vals[datePos]));
      idxList.add(idx);
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
