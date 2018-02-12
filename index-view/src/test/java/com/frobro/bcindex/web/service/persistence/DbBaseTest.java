package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.controller.ApiController;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/22/17.
 */
public abstract class DbBaseTest {

  protected PrimeRepo repo;
  protected IndexRepo oddRepo;
  protected JdbcTemplate jdbc;

  @Autowired
  public void setJdb(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Autowired
  public void setIndexRepo(IndexRepo oRepo, EvenIdxRepo eRepo,
                           TwentyRepo tRepo, TwentyEvenRepo teRepo,
                           EthRepo etRepo, EthEvenRepo eteRepo,
                           FortyIdxRepo fRepo, FortyEvenIdxRepo feRepo,
                           TotalRepo toRepo, TotalEvenRepo toeRepo,
                           CurrencyRepo cRepo, PlatformRepo pRepo,
                           ApplicationRepo aRepo) {

    this.oddRepo = oRepo;
    this.repo = PrimeRepo.getRepo(oRepo, eRepo,tRepo,teRepo,etRepo,eteRepo,fRepo,feRepo,
        toRepo,toeRepo,cRepo,pRepo,aRepo);
  }

  // we don't want the loading and updating threads spinning
  // the ApiController starts them in its init method
  @BeforeClass
  public static void turnOffApiController() {
    ApiController.turnOffThisControllerForTesting();
  }

  @Before
  @After
  public void clearDb() {
    oddRepo.deleteAll();
    repo.deleteAll();
    resetRowIds();
  }

  private void resetRowIds() {
    jdbc.execute("ALTER TABLE ODD_INDEX ALTER COLUMN ID RESTART WITH 1");
  }

  protected void populateDb(int numEntries, double price) {
    long now = System.currentTimeMillis();
    for (int i=1; i<=numEntries; i++) {
      JpaIndexTen idx = IndexFactory.getNewOdd();
      if (i == numEntries) {
        idx.setIndexValueUsd(price);
      }
      // add a minute, since that is the resolution
      // we will save things in prod
      now += TimeUnit.MINUTES.toMillis(1);
      idx.setTimeStamp(now);
      repo.saveAll(idx);
    }
  }
}
