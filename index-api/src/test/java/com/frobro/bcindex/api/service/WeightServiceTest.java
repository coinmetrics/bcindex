package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.api.service.persistence.*;
import com.frobro.bcindex.core.db.model.IndexName;
import com.frobro.bcindex.core.db.model.WeightApi;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeightServiceTest {

  private WeightService weightService;
  List<CrudRepository> repos = new ArrayList<>(8);

  @Autowired
  public void setWeightService(WeightTenRepo t,
                               WeightTwentyRepo tw,
                               WeightFortyRepo fo,
                               WeightTotalRepo to,
                               WeightEthRepo et,
                               WeightCurrencyRepo cu,
                               WeightPlatformRepo pl,
                               WeightAppRepo ap) {

    this.weightService = new WeightService(t,tw,fo,to,et,cu,pl,ap);
    repos.add(t); repos.add(tw); repos.add(fo); repos.add(to);
    repos.add(et); repos.add(cu); repos.add(pl); repos.add(ap);
  }

  @After
  public void tearDown() {
    repos.stream().forEach(r -> {r.deleteAll();});
  }

  @Test
  public void testDataMapper() {
    // given weight api populated with data
    WeightApi incomingData = new WeightApi();
    for (IndexName indexName : IndexName.values()) {
      incomingData.addIndex(indexName,createData());
    }
    long time = System.currentTimeMillis();
    incomingData.setTime(time);

    // and a data mapper
    DataMapper dataMapper = new DataMapper();
    List<DoaService> doaList = dataMapper.toDoaList(incomingData);

    // when
    weightService.save(doaList);

    // then every index should have one row saved
    for (IndexName indexName : IndexName.values()) {
      List<DoaService> doaRetreived = weightService.get(indexName);
      JsonData jsonData = new JsonData();
      jsonData.elementList = new DataMapper().toJElementList(indexName.isEven(),doaRetreived);

      // then
      assertEquals(indexName.name(), incomingData.getWeight(indexName),jsonData.elementList.get(0).dataMap);
    }
  }

  private Map<String,Double> createData() {
    Map<String,Double> map = new HashMap<>();
    map.put("btc",randDouble());
    map.put("eth",randDouble());
    return map;
  }

  private double randDouble() {
    return ThreadLocalRandom.current().nextDouble(0, 101);
  }

  @Test
  public void testAllIndices() {
    for (IndexName idx : IndexName.values()) {
      if (not(idx.isEven())) {
        test(idx);
      }
    }
  }

  private boolean not(boolean b) {
    return !b;
  }

  private void test(IndexName index) {
    // given
    DoaService doa = new DoaService();
    Map<String,Double> data = new HashMap<>();
    data.put("btc",30.0);
    data.put("eth",20.0);
    doa.setDataMap(data);

    if (index.hasEven()) {
      setEven(doa);
    }
    doa.setName(index);
    doa.setTime(System.currentTimeMillis());

    // when
    weightService.save(Arrays.asList(doa));
    // and
    List<DoaService> retreivedList = weightService.getFullIndex(index);

    // then
    assertEquals(1, retreivedList.size());
    // and
    assertEquals(doa,retreivedList.get(0));
  }

  private void setEven(DoaService doa) {
    Map<String,Double> data = new HashMap<>();
    data.put("btc",50.0);
    data.put("eth",10.0);
    doa.setDataMapEven(data);
  }
}
