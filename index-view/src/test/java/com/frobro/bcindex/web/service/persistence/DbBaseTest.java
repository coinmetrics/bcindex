package com.frobro.bcindex.web.service.persistence;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by rise on 5/22/17.
 */
public abstract class DbBaseTest {

  protected IndexRepo oddRepo;
  protected EvenIdxRepo evenRepo;
  protected JdbcTemplate jdbc;

  @Autowired
  public void setJdb(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Autowired
  public void setIndexRepo(IndexRepo repo, EvenIdxRepo eRepo) {
    this.oddRepo = repo;
    this.evenRepo = eRepo;
  }

  @Before
  @After
  public void clearDb() {
    oddRepo.deleteAll();
    evenRepo.deleteAll();
    resetRowIds();
  }

  private void resetRowIds() {
    jdbc.execute("ALTER TABLE ODD_INDEX ALTER COLUMN ID RESTART WITH 1");
  }
}
