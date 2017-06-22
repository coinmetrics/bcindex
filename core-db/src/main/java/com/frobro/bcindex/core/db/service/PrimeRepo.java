package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.*;

/**
 * Created by rise on 6/20/17.
 */
public class PrimeRepo {

  private static PrimeRepo instance;

  private long idxTenCnt = 0;
  private long idxEvenTenCnt = 0;
  private long idxTwentyCnt = 0;
  private long idxTwentyEvenCnt = 0;

  private IndexRepo indexRepo;
  private EvenIdxRepo evenIdxRepo;
  private TwentyRepo twentyRepo;
  private TwentyEvenRepo twentyEvenRepo;

  public static PrimeRepo getRepo(IndexRepo ir, EvenIdxRepo eir,
                                  TwentyRepo tr, TwentyEvenRepo ter) {
    if (instance == null) {
      instance = new PrimeRepo(ir,eir,tr,ter);
    }
    return instance;
  }

  private PrimeRepo(IndexRepo ir, EvenIdxRepo eir,
                   TwentyRepo tr, TwentyEvenRepo ter) {
    this.indexRepo = ir;
    this.evenIdxRepo = eir;
    this.twentyRepo = tr;
    this.twentyEvenRepo = ter;
    initCounts();
  }

  private void initCounts() {
    idxTenCnt = indexRepo.count();
    idxEvenTenCnt = evenIdxRepo.count();
    idxTwentyCnt = twentyRepo.count();
    idxTwentyEvenCnt = twentyEvenRepo.count();
  }

  public void saveTen(JpaIndexTen idx) {
    idxTenCnt++;
    idx.setBletchId(idxTenCnt);
    indexRepo.save(idx);
  }

  public void saveTenEven(JpaEvenIndex idx) {
    idxEvenTenCnt++;
    idx.setBletchId(idxEvenTenCnt);
    evenIdxRepo.save(idx);
  }

  public void saveTwenty(JpaIdxTwenty idx) {
    idxTwentyCnt++;
    idx.setBletchId(idxTwentyCnt);
    twentyRepo.save(idx);
  }

  public void saveEvenTwenty(JpaTwentyEven idx) {
    idxTwentyEvenCnt++;
    idx.setBletchId(idxTwentyEvenCnt);
    twentyEvenRepo.save(idx);
  }

  public void deleteAll() {
    this.indexRepo.deleteAll();
    this.evenIdxRepo.deleteAll();
    this.twentyRepo.deleteAll();
    this.twentyEvenRepo.deleteAll();
  }
}
