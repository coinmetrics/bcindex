package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.service.*;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

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
                           EthRepo etRepo, EthEvenRepo eteRepo) {

    this.oddRepo = oRepo;
    this.repo = PrimeRepo.getRepo(oRepo, eRepo,tRepo,teRepo,etRepo,eteRepo);
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
}
