package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.EvenIdxRepo;
import com.frobro.bcindex.core.db.service.IndexRepo;
import com.frobro.bcindex.core.db.service.TwentyEvenRepo;
import com.frobro.bcindex.core.db.service.TwentyRepo;
import com.frobro.bcindex.web.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by rise on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexRepoTest {

  private IndexRepo indexRepo;
  private EvenIdxRepo evenRepo;
  private TwentyRepo twentyRepo;
  private TwentyEvenRepo twentyEvenRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo,
                           EvenIdxRepo evenRepo,
                           TwentyRepo trepo,
                           TwentyEvenRepo terepo) {
    this.indexRepo = repo;
    this.evenRepo = evenRepo;
    this.twentyRepo = trepo;
    this.twentyEvenRepo = terepo;
  }

  @Test
  public void testSaveTen() {
    JpaIndexTen index = new JpaIndexTen();
    populate(index);

    indexRepo.save(index);
    JpaIndexTen retreived = indexRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveTenEven() {
    JpaEvenIndex index = new JpaEvenIndex();
    populate(index);

    evenRepo.save(index);
    JpaEvenIndex retreived = evenRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveTwenty() {
    JpaIdxTwenty index = new JpaIdxTwenty();
    populate(index);

    twentyRepo.save(index);
    JpaIdxTwenty retreived = twentyRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveTwentyEven() {
    JpaTwentyEven index = new JpaTwentyEven();
    populate(index);

    twentyEvenRepo.save(index);
    JpaTwentyEven retreived = twentyEvenRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    validate(index, retreived);
  }

  private void populate(JpaIndex idx) {
    idx.setIndexValueBtc(12)
        .setIndexValueUsd(34)
        .setId(1234567L);
  }

  private void validate(JpaIndex expected, JpaIndex retreived) {
    assertEquals(expected.getIndexValueBtc(), retreived.getIndexValueBtc(), 0.01);
    assertEquals(expected.getIndexValueUsd(), retreived.getIndexValueUsd(), 0.01);
  }
}
