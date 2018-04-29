package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.domain.weight.*;
import com.frobro.bcindex.api.service.weight.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeightService {
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

  public void saveTen(Map<String, Double> dataMap,
                      Map<String, Double> dataMapEven,
                      long time) {

    final List<JpaWeightTen> dataList = new ArrayList<>(dataMap.size());
    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightTen jpa = new JpaWeightTen();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    for (Map.Entry<String, Double> entry : dataMapEven.entrySet()) {
      JpaWeightTen jpa = new JpaWeightTen();
      jpa.setIndexName("ten even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    this.tenRepo.saveAll(dataList);
  }

  public void saveTwenty(Map<String, Double> dataMap,
                         Map<String, Double> dataMapEven,
                         long time) {

    final List<JpaWeightTwenty> dataList = new ArrayList<>(dataMap.size());

    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightTwenty jpa = new JpaWeightTwenty();
      jpa.setIndexName("twenty even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    for (Map.Entry<String, Double> entry : dataMapEven.entrySet()) {
      JpaWeightTwenty jpa = new JpaWeightTwenty();
      jpa.setIndexName("twenty even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.twentyRepo.saveAll(dataList);
  }

  public void saveForty(Map<String, Double> dataMap,
                        Map<String, Double> dataMapEven,
                        long time) {

    final List<JpaWeightForty> dataList = new ArrayList<>(dataMap.size());
    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightForty jpa = new JpaWeightForty();
      jpa.setIndexName("forty index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    for (Map.Entry<String, Double> entry : dataMapEven.entrySet()) {
      JpaWeightForty jpa = new JpaWeightForty();
      jpa.setIndexName("ten even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.fortyRepo.saveAll(dataList);
  }

  public void saveTotal(Map<String, Double> dataMap,
                        Map<String, Double> dataMapEven,
                        long time) {

    final List<JpaWeightTotal> dataList = new ArrayList<>(dataMap.size());

    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightTotal jpa = new JpaWeightTotal();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    for (Map.Entry<String, Double> entry : dataMapEven.entrySet()) {
      JpaWeightTotal jpa = new JpaWeightTotal();
      jpa.setIndexName("ten even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.totalRepo.saveAll(dataList);
  }

  public void saveEther(Map<String, Double> dataMap,
                        Map<String, Double> dataMapEven,
                        long time) {

    final List<JpaWeightEther> dataList = new ArrayList<>(dataMap.size());

    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightEther jpa = new JpaWeightEther();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }

    for (Map.Entry<String, Double> entry : dataMapEven.entrySet()) {
      JpaWeightEther jpa = new JpaWeightEther();
      jpa.setIndexName("ten even index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.ethRepo.saveAll(dataList);
  }

  public void saveCurrency(Map<String, Double> dataMap, long time) {
    final List<JpaCurrency> dataList = new ArrayList<>(dataMap.size());

    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaCurrency jpa = new JpaCurrency();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.currRepo.saveAll(dataList);
  }

  public void savePlatform(Map<String, Double> dataMap, long time) {
    final List<JpaPlatform> dataList = new ArrayList<>(dataMap.size());
    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaPlatform jpa = new JpaPlatform();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.platformRepo.saveAll(dataList);
  }

  public void saveApp(Map<String, Double> dataMap, long time) {
    final List<JpaWeightApp> dataList = new ArrayList<>(dataMap.size());
    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      JpaWeightApp jpa = new JpaWeightApp();
      jpa.setIndexName("ten index")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time);
      dataList.add(jpa);
    }
    this.appRepo.saveAll(dataList);
  }

  private void convertToJpa(Map<String, Double> dataMap,
                            List<JpaWeight> dataList,
                            Class<? extends JpaWeight> clazz,
                            long time) {

    for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
      dataList.add(new JpaWeightTen()
          .setIndexName("")
          .setTicker(entry.getKey())
          .setWeight(entry.getValue())
          .setTimeStamp(time));
    }
  }
}