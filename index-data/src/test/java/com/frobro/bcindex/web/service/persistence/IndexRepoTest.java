package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
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
  private EthRepo ethRepo;
  private EthEvenRepo ethEvenRepo;
  private PrimeRepo primeRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo,
                           EvenIdxRepo eRepo,
                           TwentyRepo trepo,
                           TwentyEvenRepo terepo,
                           EthRepo etRepo,
                           EthEvenRepo eteRepo) {
    this.indexRepo = repo;
    this.evenRepo = eRepo;
    this.twentyRepo = trepo;
    this.twentyEvenRepo = terepo;
    this.ethRepo = etRepo;
    this.ethEvenRepo = eteRepo;

    primeRepo = PrimeRepo.getRepo(indexRepo,evenRepo,
                              twentyRepo,twentyEvenRepo,
                              ethRepo, ethEvenRepo);
  }

  @Test
  public void testSaveTen() {
    JpaIndexTen index = new JpaIndexTen();
    populate(index);

    primeRepo.saveTen(index);
    JpaIndexTen retreived = indexRepo.findByTimeStamp(index.getTimeStamp().getTime()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveTenEven() {
    JpaEvenIndex index = new JpaEvenIndex();
    populate(index);

    primeRepo.saveTenEven(index);
    JpaEvenIndex retreived = evenRepo.findByTimeStamp(index.getTimeStamp().getTime()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveEth() {
    JpaIdxEth idx = new JpaIdxEth();
    populate(idx);

    primeRepo.saveEth(idx);
    JpaIdxEth retreived = ethRepo.findFirstByOrderByIdDesc();

    validate(idx, retreived);
  }

  @Test
  public void testSaveTwenty() {
    JpaIdxTwenty index = new JpaIdxTwenty();
    populate(index);

    primeRepo.saveTwenty(index);
    JpaIdxTwenty retreived = twentyRepo.findByTimeStamp(index.getTimeStamp().getTime()).get(0);

    validate(index, retreived);
  }

  @Test
  public void testSaveTwentyEven() {
    JpaTwentyEven index = new JpaTwentyEven();
    populate(index);

    primeRepo.saveEvenTwenty(index);
    JpaTwentyEven retreived = twentyEvenRepo.findByTimeStamp(index.getTimeStamp().getTime()).get(0);

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
