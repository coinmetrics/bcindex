package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import org.junit.After;
import org.junit.Before;
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
                           FortyIdxRepo fRepo, FortyEvenIdxRepo feRepo) {

    this.oddRepo = oRepo;
    this.repo = PrimeRepo.getRepo(oRepo, eRepo,tRepo,teRepo,etRepo,eteRepo,fRepo,feRepo);
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
      repo.saveTen(idx);
      saveAll(idx);
    }
  }

  private void saveAll(JpaIndex idx) {
    JpaEvenIndex eTen = new JpaEvenIndex();
    set(idx, eTen);
    repo.saveTenEven(eTen);

    JpaIdxTwenty i20 = new JpaIdxTwenty();
    set(idx, i20);
    repo.saveTwenty(i20);

    JpaTwentyEven ie20 = new JpaTwentyEven();
    set(idx, ie20);
    repo.saveEvenTwenty(ie20);

    JpaIdxEth eth = new JpaIdxEth();
    set(idx, eth);
    repo.saveEth(eth);

    JpaIdxEthEven eEth = new JpaIdxEthEven();
    set(idx, eEth);
    repo.saveEthEven(eEth);
  }

  private void set(JpaIndex src, JpaIndex desc) {
    desc.setBletchId(src.getBletchId());
    desc.setIndexValueBtc(src.getIndexValueBtc());
    desc.setIndexValueUsd(src.getIndexValueUsd());
    desc.setTimeStamp(src.getTimeStamp());
    desc.setId(src.getId());
  }
}
