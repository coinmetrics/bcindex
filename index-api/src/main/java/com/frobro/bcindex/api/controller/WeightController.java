package com.frobro.bcindex.api.controller;

import com.frobro.bcindex.api.IndexApiApp;
import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.api.model.JsonElement;
import com.frobro.bcindex.api.service.DataMapper;
import com.frobro.bcindex.api.service.WeightCache;
import com.frobro.bcindex.api.service.WeightService;
import com.frobro.bcindex.api.service.persistence.*;
import com.frobro.bcindex.core.db.model.IndexName;
import com.frobro.bcindex.core.db.model.WeightApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class WeightController {
  private static final Logger LOG = LoggerFactory.getLogger(WeightController.class);
  DataMapper dataMapper = new DataMapper();
  private WeightService weightService;
  private WeightCache cache = new WeightCache();


  @Autowired
  public void initWeightRepo(WeightTenRepo ten,
                             WeightTwentyRepo twenty,
                             WeightFortyRepo forty,
                             WeightTotalRepo total,
                             WeightEthRepo eth,
                             WeightCurrencyRepo curr,
                             WeightPlatformRepo plat,
                             WeightAppRepo app) {

    weightService = new WeightService(
        ten,twenty,forty,total,eth,curr,plat,app);
  }

  @PostConstruct
  public void init() {
    loadCache();
  }

  @RequestMapping("/amup")
  public String dataWeight() {
    return IndexApiApp.class.getName() + " is running";
  }

  @RequestMapping(value = "/daily/weight", method = RequestMethod.POST)
  public String getDailyWeights(@RequestBody IndexName indexName) {
    return cache.getResonse(indexName);
  }


  /* receive data */
  @RequestMapping(value = "blet/weight/daily", method = RequestMethod.POST)
  public void cacheUpdate(@RequestBody WeightApi data) {
    System.out.println("receivded data " + data);

    if (data != null && data.amSecure()) {
      data.removeKeyIfExists();
      processData(data);
    }
  }


  private void processData(WeightApi data) {
    cache.update(null);
    System.out.println("processing data " + data);

    weightService.save(dataMapper.toDoaList(data));
  }

  private void loadCache() {
    for (IndexName indexName : IndexName.values()) {
      cache.update(dataMapper.toJsonData(indexName, weightService.get(indexName)));
    }
  }
}
