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
  private long idxEthCnt = 0;
  private long idxEthEvenCnt = 0;
  private long idxFortyCnt = 0;
  private long idxFortyEvenCnt = 0;
  private long idxTotalCnt = 0;
  private long idxTotalEvenCnt = 0;
  private long idxCurrencyCnt = 0;
  private long idxPlatformCnt = 0;
  private long idxApplicationCnt = 0;

  private IndexRepo indexRepo;
  private EvenIdxRepo evenIdxRepo;
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
  private ApplicationRepo applicationRepo;

  public static PrimeRepo getRepo(IndexRepo ir, EvenIdxRepo eir,
                                  TwentyRepo tr, TwentyEvenRepo ter,
                                  EthRepo er, EthEvenRepo eer,
                                  FortyIdxRepo fr, FortyEvenIdxRepo fer,
                                  TotalRepo t, TotalEvenRepo te,
                                  CurrencyRepo cr, PlatformRepo pr,
                                  ApplicationRepo ap) {
    if (instance == null) {
      instance = new PrimeRepo(ir,eir,tr,ter, er, eer, fr, fer,
          t, te, cr, pr, ap);
    }
    return instance;
  }

  private PrimeRepo(IndexRepo ir, EvenIdxRepo eir,
                   TwentyRepo tr, TwentyEvenRepo ter,
                    EthRepo er, EthEvenRepo eer,
                    FortyIdxRepo fr, FortyEvenIdxRepo fer,
                    TotalRepo t, TotalEvenRepo te,
                    CurrencyRepo cr, PlatformRepo pr,
                    ApplicationRepo ap) {

    this.indexRepo = ir;
    this.evenIdxRepo = eir;
    this.twentyRepo = tr;
    this.twentyEvenRepo = ter;
    this.ethRepo = er;
    this.ethEvenRepo = eer;
    this.fortyRepo = fr;
    this.fortyEvenRepo = fer;
    this.totalRepo = t;
    this.totalEvenRepo = te;
    this.currencyRepo = cr;
    this.platformRepo = pr;
    this.applicationRepo = ap;
    initCounts();
  }

  private void initCounts() {
    idxTenCnt = initTen();
    idxEvenTenCnt = initEvenTen();
    idxTwentyCnt = initTwenty();
    idxTwentyEvenCnt = initTwentyEven();
    idxEthCnt = initEth();
    idxEthEvenCnt = initEthEven();
    idxFortyCnt = initForty();
    idxFortyEvenCnt = initFortyEven();
    idxTotalCnt = initTotal();
    idxTotalEvenCnt = initTotalEven();
    idxCurrencyCnt = initCurrency();
    idxPlatformCnt = initPlatform();
    idxApplicationCnt = initApplication();
  }

  private long initCurrency() {
    long cnt = currencyRepo.count();
    if (cnt != 0) {
      JpaCurrency idx = currencyRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initPlatform() {
    long cnt = platformRepo.count();
    if (cnt != 0) {
      JpaPlatform idx = platformRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initApplication() {
    long cnt = applicationRepo.count();
    if (cnt != 0) {
      JpaApplication idx = applicationRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initTotal() {
    long cnt = totalRepo.count();
    if (cnt != 0) {
      JpaIndexTotal idx = totalRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initTotalEven() {
    long cnt = totalEvenRepo.count();
    if (cnt != 0) {
      JpaIndexTotalEven idx = totalEvenRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initForty() {
    long cnt = fortyRepo.count();
    if (cnt != 0) {
      JpaIdxForty idx = fortyRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initFortyEven() {
    long cnt = fortyEvenRepo.count();
    if (cnt != 0) {
      JpaIdxFortyEven idx = fortyEvenRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initEth() {
    long cnt = ethRepo.count();
    if (cnt != 0) {
      JpaIdxEth idx = ethRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initEthEven() {
    long cnt = ethEvenRepo.count();
    if (cnt != 0) {
      JpaIdxEthEven idx = ethEvenRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initTen() {
    long cnt = indexRepo.count();

    if (cnt != 0) {
      JpaIndexTen idx = indexRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initEvenTen() {
    long cnt = evenIdxRepo.count();
    if (cnt != 0) {
      JpaEvenIndex idx = evenIdxRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initTwenty() {
    long cnt = twentyRepo.count();
    if (cnt != 0) {
      JpaIdxTwenty idx = twentyRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
  }

  private long initTwentyEven() {
    long cnt = twentyEvenRepo.count();
    if (cnt != 0) {
      JpaTwentyEven idx = twentyEvenRepo.findFirstByOrderByIdDesc();
      cnt = idx.getBletchId();
    }
    return cnt;
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

  public void saveEth(JpaIdxEth idx) {
    idxEthCnt++;
    idx.setBletchId(idxEthCnt);
    ethRepo.save(idx);
  }

  public void saveEthEven(JpaIdxEthEven idx) {
    idxEthEvenCnt++;
    idx.setBletchId(idxEthEvenCnt);
    ethEvenRepo.save(idx);
  }

  public void saveForty(JpaIdxForty idx) {
    idxFortyCnt++;
    idx.setBletchId(idxFortyCnt);
    fortyRepo.save(idx);
  }

  public void saveFortyEven(JpaIdxFortyEven idx) {
    idxFortyEvenCnt++;
    idx.setBletchId(idxFortyEvenCnt);
    fortyEvenRepo.save(idx);
  }

  public void saveTotal(JpaIndexTotal idx) {
    idxTotalCnt++;
    idx.setBletchId(idxTotalCnt);
    totalRepo.save(idx);
  }

  public void saveTotalEven(JpaIndexTotalEven idx) {
    idxTotalEvenCnt++;
    idx.setBletchId(idxTotalEvenCnt);
    totalEvenRepo.save(idx);
  }

  public void saveCurrency(JpaCurrency idx) {
    idxCurrencyCnt++;
    idx.setBletchId(idxCurrencyCnt);
    currencyRepo.save(idx);
  }

  public void savePlatform(JpaPlatform idx) {
    idxPlatformCnt++;
    idx.setBletchId(idxPlatformCnt);
    platformRepo.save(idx);
  }

  public void saveAplication(JpaApplication idx) {
    idxApplicationCnt++;
    idx.setBletchId(idxApplicationCnt);
    applicationRepo.save(idx);
  }

  public void saveAll(JpaIndex idx) {
    JpaIndexTen ten = new JpaIndexTen();
    set(idx, ten);
    saveTen(ten);

    JpaEvenIndex eTen = new JpaEvenIndex();
    set(idx, eTen);
    saveTenEven(eTen);

    JpaIdxTwenty i20 = new JpaIdxTwenty();
    set(idx, i20);
    saveTwenty(i20);

    JpaTwentyEven ie20 = new JpaTwentyEven();
    set(idx, ie20);
    saveEvenTwenty(ie20);

    JpaIdxFortyEven i40 = new JpaIdxFortyEven();
    set(idx, i40);
    saveFortyEven(i40);

    JpaIdxForty ie40 = new JpaIdxForty();
    set(idx, ie40);
    saveForty(ie40);

    JpaIdxEth eth = new JpaIdxEth();
    set(idx, eth);
    saveEth(eth);

    JpaIdxEthEven eEth = new JpaIdxEthEven();
    set(idx, eEth);
    saveEthEven(eEth);

    JpaIndexTotal tot = new JpaIndexTotal();
    set(idx, tot);
    saveTotal(tot);

    JpaIndexTotalEven eTot = new JpaIndexTotalEven();
    set(idx, eTot);
    saveTotalEven(eTot);

    JpaCurrency curr = new JpaCurrency();
    set(idx, curr);
    saveCurrency(curr);

    JpaPlatform plat = new JpaPlatform();
    set(idx, plat);
    savePlatform(plat);

    JpaApplication app = new JpaApplication();
    set(idx, app);
    saveAplication(app);
  }

  private void set(JpaIndex src, JpaIndex desc) {
    desc.setBletchId(src.getBletchId());
    desc.setIndexValueBtc(src.getIndexValueBtc());
    desc.setIndexValueUsd(src.getIndexValueUsd());
    desc.setTimeStamp(src.getTimeStamp());
    desc.setId(src.getId());
  }

  public void deleteAll() {
    this.indexRepo.deleteAll();
    this.evenIdxRepo.deleteAll();
    this.twentyRepo.deleteAll();
    this.twentyEvenRepo.deleteAll();
    this.ethRepo.deleteAll();
    this.ethEvenRepo.deleteAll();
    this.fortyRepo.deleteAll();
    this.fortyEvenRepo.deleteAll();
    this.totalRepo.deleteAll();
    this.totalEvenRepo.deleteAll();
    this.currencyRepo.deleteAll();
    this.platformRepo.deleteAll();
    this.applicationRepo.deleteAll();
  }
}

