package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.service.persistence.EvenIdxRepo;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/20/17.
 */
@RestController
@Profile({"dev","pgres"})
public class SeedController {

  private EvenIdxRepo evenRepo;
  private IndexRepo oddRepo;
  private long start = System.currentTimeMillis();

  @Autowired
  public void setRepos(EvenIdxRepo eRepo, IndexRepo oRepo) {
    this.evenRepo = eRepo;
    this.oddRepo = oRepo;
  }

  @RequestMapping("/seed")
  public String seed() {
    int numHours = 20;
    int numIterations = (int) TimeUnit.HOURS.toMinutes(numHours);
    for (int i=0; i<numIterations; i++) {
      evenRepo.save(newEven());
      oddRepo.save(newOdd());
    }
    return "done";
  }

  private JpaIndex newOdd() {
    double nextDouble = nextDouble();
    return new JpaIndex()
        .setTimeStamp(nextMinute())
        .setIndexValueUsd(nextDouble)
        .setIndexValueBtc(nextDouble/10.0);
  }

  private JpaEvenIndex newEven() {
    double nextDouble = nextDouble();
    return new JpaEvenIndex()
        .setTimeStamp(nextMinute())
        .setIndexValueUsd(nextDouble)
        .setIndexValueBtc(nextDouble/10.0);
  }

  private double nextDouble() {
    Random random = new Random();
    double value = 50.0 + (150.0 - 50.0) * random.nextDouble();
    return value;
  }
  private long nextMinute() {
    return start + TimeUnit.MINUTES.toMillis(1);
  }
}
