package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.domain.weight.*;
import com.frobro.bcindex.api.service.persistence.*;
import com.frobro.bcindex.core.db.model.IndexName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WeightService {
  private static final Logger LOG = LoggerFactory.getLogger(WeightService.class);
  private long tenBletchId;
  private long twentyBletchId;
  private long fortyBletchId;
  private long totalBletchId;
  private long ethBletchId;
  private long currBletchId;
  private long platformBletchId;
  private long appBletchId;

  private WeightTenRepo tenRepo;
  private WeightTwentyRepo twentyRepo;
  private WeightFortyRepo fortyRepo;
  private WeightTotalRepo totalRepo;
  private WeightEthRepo ethRepo;
  private WeightCurrencyRepo currRepo;
  private WeightPlatformRepo platformRepo;
  private WeightAppRepo appRepo;

  public WeightService(WeightTenRepo t,
                       WeightTwentyRepo te,
                       WeightFortyRepo f,
                       WeightTotalRepo to,
                       WeightEthRepo e,
                       WeightCurrencyRepo c,
                       WeightPlatformRepo p,
                       WeightAppRepo a) {
    this.tenRepo = t;
    this.twentyRepo = te;
    this.fortyRepo = f;
    this.totalRepo = to;
    this.ethRepo = e;
    this.currRepo = c;
    this.platformRepo = p;
    this.appRepo = a;
  }

  public void save(List<DoaService> doaList) {
    doaList.forEach(doa -> {
      save(doa);
    });
  }

  private void save(DoaService doa) {
    IndexName idx = doa.getName();
    switch (idx) {
      case TEN:
        saveTen(doa);
        break;
      case TWENTY:
        saveTwenty(doa);
        break;
      case FORTY:
        saveForty(doa);
        break;
      case TOTAL:
        saveTotal(doa);
        break;
      case ETHEREUM:
        saveEthereum(doa);
        break;
      case CURRENCY:
        saveCurrency(doa);
        break;
      case PLATFORM:
        savePlatform(doa);
        break;
      case APPLICATION:
        saveApplication(doa);
        break;
    }
  }

  private void saveTen(DoaService doa) {
    JpaWeightTen data = new JpaWeightTen();
    populate(doa,tenBletchId++, data);
    this.tenRepo.save(data);
  }

  private void saveTwenty(DoaService doa) {
    JpaWeightTwenty data = new JpaWeightTwenty();
    populate(doa,twentyBletchId++, data);
    this.twentyRepo.save(data);
  }

  private void saveForty(DoaService doa) {
    JpaWeightForty data = new JpaWeightForty();
    populate(doa,fortyBletchId++, data);
    this.fortyRepo.save(data);
  }

  private void saveTotal(DoaService doa) {
    JpaWeightTotal data = new JpaWeightTotal();
    populate(doa,totalBletchId++, data);
    this.totalRepo.save(data);
  }

  private void saveEthereum(DoaService doa) {
    JpaWeightEther data = new JpaWeightEther();
    populate(doa,ethBletchId++, data);
    this.ethRepo.save(data);
  }

  private void saveCurrency(DoaService doa) {
    JpaCurrency data = new JpaCurrency();
    populate(doa,currBletchId++, data);
    this.currRepo.save(data);
  }

  private void savePlatform(DoaService doa) {
    JpaPlatform data = new JpaPlatform();
    populate(doa,platformBletchId++, data);
    this.platformRepo.save(data);
  }

  private void saveApplication(DoaService doa) {
    JpaWeightApp data = new JpaWeightApp();
    populate(doa,appBletchId++, data);
    this.appRepo.save(data);
  }

  private void populate(DoaService doa, long bid, JpaWeight jpa) {
    jpa.populate(doa);
    jpa.setBletchId(bid);
  }

  public List<DoaService> get(IndexName indexName) {
    List<DoaService> list;

    if (indexName.isEven()) {
      list = getFullIndex(indexName.getEvenMatch());
    }
    else {
      list = getFullIndex(indexName);
    }
    return list;
  }

  public List<DoaService> getFullIndex(IndexName indexName) {
    List<DoaService> list = new ArrayList<>();
    switch (indexName) {
      case TEN:
        list = getList(tenRepo.findAll());
        break;
      case TWENTY:
        list = getList(twentyRepo.findAll());
        break;
      case FORTY:
        list = getList(fortyRepo.findAll());
        break;
      case TOTAL:
        list = getList(totalRepo.findAll());
        break;
      case ETHEREUM:
        list = getList(ethRepo.findAll());
        break;
      case CURRENCY:
        list = getList(currRepo.findAll());
        break;
      case PLATFORM:
        list = getList(platformRepo.findAll());
        break;
      case APPLICATION:
        list = getList(appRepo.findAll());
        break;
      default:
        LOG.error("could not find index: " + indexName);
        break;
    }
    return list;
  }

  private List<DoaService> getList(Iterable<? extends JpaWeight> iterable) {
    List<DoaService> doaList = new LinkedList<>();
    iterable.forEach(jpa -> {
      doaList.add(jpa.getDoa());
    });
    return doaList;
  }
}