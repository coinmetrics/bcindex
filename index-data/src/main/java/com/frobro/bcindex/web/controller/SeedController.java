package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.service.persistence.EvenIdxRepo;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/20/17.
 */
@RestController
@Profile({"dev","pgres"})
public class SeedController {

  private static final BcLog log = BcLog.getLogger(SeedController.class);
  private EvenIdxRepo evenRepo;
  private IndexRepo oddRepo;

  @Autowired
  public void setRepos(EvenIdxRepo eRepo, IndexRepo oRepo) {
    this.evenRepo = eRepo;
    this.oddRepo = oRepo;
  }

  @PostConstruct
  public void start(){
    log.info("populating the database with mock data ...");
    seed();
  }

  @RequestMapping("/seed")
  public String seed() {
    int numHours = 200;
    int numIterations = (int) TimeUnit.HOURS.toMinutes(numHours);

    List<JpaEvenIndex> evenList = new ArrayList<>(numIterations);
    List<JpaIndex> oddList = new ArrayList<>(numIterations);
    long time = System.currentTimeMillis();
    for (int i=0; i<numIterations; i++) {
      time  += nextMinute();
      evenList.add(newEven(time));
      oddList.add(newOdd(time));
    }
    evenRepo.save(evenList);
    oddRepo.save(oddList);

    return "done";
  }

  private JpaIndex newOdd(long now) {
    double nextDouble = nextDouble();
    return new JpaIndex()
        .setTimeStamp(now)
        .setIndexValueUsd(nextDouble)
        .setIndexValueBtc(nextDouble / 10.0);
  }

  private JpaEvenIndex newEven(long now) {
    double nextDouble = nextDouble();
    return new JpaEvenIndex()
        .setTimeStamp(now)
        .setIndexValueUsd(nextDouble)
        .setIndexValueBtc(nextDouble / 10.0);
  }

  private double nextDouble() {
    Random random = new Random();
    double value = 50.0 + (150.0 - 50.0) * random.nextDouble();
    return value;
  }
  private long nextMinute() {
    return TimeUnit.MINUTES.toMillis(1);
  }
}
