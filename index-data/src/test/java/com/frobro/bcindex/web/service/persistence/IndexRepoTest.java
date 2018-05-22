package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

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
  private FortyIdxRepo fortyRepo;
  private FortyEvenIdxRepo fortyEvenRepo;
  private TotalRepo totalRepo;
  private TotalEvenRepo totalEvenRepo;
  private CurrencyRepo currencyRepo;
  private PlatformRepo platformRepo;
  private ApplicationRepo appRepo;
  private PrimeRepo primeRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo,
                           EvenIdxRepo eRepo,
                           TwentyRepo trepo,
                           TwentyEvenRepo terepo,
                           EthRepo etRepo,
                           EthEvenRepo eteRepo,
                           FortyIdxRepo fRepo,
                           FortyEvenIdxRepo feRepo,
                           TotalRepo toRepo,
                           TotalEvenRepo toeRepo,
                           CurrencyRepo cRepo,
                           PlatformRepo pRepo,
                           ApplicationRepo aRepo) {
    this.indexRepo = repo;
    this.evenRepo = eRepo;
    this.twentyRepo = trepo;
    this.twentyEvenRepo = terepo;
    this.ethRepo = etRepo;
    this.ethEvenRepo = eteRepo;
    this.fortyRepo = fRepo;
    this.fortyEvenRepo = feRepo;
    this.totalRepo = toRepo;
    this.totalEvenRepo = toeRepo;
    this.currencyRepo = cRepo;
    this.platformRepo = pRepo;
    this.appRepo = aRepo;

    primeRepo = PrimeRepo.getRepo(indexRepo,evenRepo,
                              twentyRepo,twentyEvenRepo,
                              ethRepo, ethEvenRepo,
                              fRepo, feRepo,
                              toRepo, toeRepo,
                              cRepo,pRepo,aRepo);
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

  @Test
  public void testSaveForty() {
    JpaIdxForty forty = new JpaIdxForty();
    populate(forty);
    primeRepo.saveForty(forty);
    JpaIdxForty retrieved = fortyRepo.findFirstByOrderByIdDesc();
    validate(forty, retrieved);

    JpaIdxFortyEven fortyEven = new JpaIdxFortyEven();
    populate(fortyEven);
    primeRepo.saveFortyEven(fortyEven);
    JpaIdxFortyEven retrievedEven = fortyEvenRepo.findFirstByOrderByIdDesc();
    validate(forty, retrievedEven);
  }

  @Test
  public void testSaveTotal() {
    JpaIndexTotal total = new JpaIndexTotal();
    populate(total);
    primeRepo.saveTotal(total);
    JpaIndexTotal retrieved = totalRepo.findFirstByOrderByIdDesc();
    validate(total, retrieved);

    JpaIndexTotalEven totalEven = new JpaIndexTotalEven();
    populate(totalEven);
    primeRepo.saveTotalEven(totalEven);
    JpaIndexTotalEven retrievedEven = totalEvenRepo.findFirstByOrderByIdDesc();
    validate(totalEven, retrievedEven);
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
