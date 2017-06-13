package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.EvenIdxRepo;
import com.frobro.bcindex.core.db.service.IndexRepo;
import com.frobro.bcindex.core.db.service.TwentyEvenRepo;
import com.frobro.bcindex.core.db.service.TwentyRepo;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.TickerService;
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
    tickerService.setIndexRepo(oRepo, eRepo);
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
    int numHours = 26;
    int numIterations = (int) TimeUnit.HOURS.toMinutes(numHours);

    List<JpaEvenIndex> evenList = new ArrayList<>(numIterations);
    List<JpaIndexTen> oddList = new ArrayList<>(numIterations);
    List<JpaIdxTwenty> list20 = new ArrayList<>(numIterations);
    List<JpaTwentyEven> listEven20 = new ArrayList<>(numIterations);
    long time = System.currentTimeMillis();
    for (int i=0; i<numIterations; i++) {
      time  += nextMinute();
      evenList.add(newEven(time));
      oddList.add(newOdd(time));
      list20.add(newTwenty(time));
      listEven20.add(new20Even(time));
    }
    evenRepo.save(evenList);
    oddRepo.save(oddList);
    twentyRepo.save(list20);
    twentyEvenRepo.save(listEven20);

    return "done seeding";
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
