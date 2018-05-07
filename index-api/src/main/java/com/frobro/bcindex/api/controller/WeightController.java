package com.frobro.bcindex.api.controller;

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

  @RequestMapping(value = "/daily/weight", method = RequestMethod.POST)
  public String getDailyWeights(@RequestBody IndexName indexName) {
    return cache.getResonse(indexName);
  }

  @RequestMapping("/data/weight")
  public String dataWeight() {
    return "accept weights";
  }

  @RequestMapping(value = "blet/weight", method = RequestMethod.POST)
  public void cacheUpdate(@RequestBody WeightApi data) {
    if (data != null && data.amSecure()) {
      data.removeKeyIfExists();
      processData(data);
    }
  }


  private void processData(WeightApi data) {
    cache.update(null);
    weightService.save(dataMapper.toDoaList(data));
  }

  private void loadCache() {
    for (IndexName indexName : IndexName.values()) {
      cache.update(dataMapper.toJsonData(indexName, weightService.get(indexName)));
    }
  }
}
