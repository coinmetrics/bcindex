package com.frobro.bcindex.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.api.IndexApiApp;
import com.frobro.bcindex.api.model.IndexDto;
import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.api.model.JsonElement;
import com.frobro.bcindex.api.service.DataMapper;
import com.frobro.bcindex.api.service.WeightCache;
import com.frobro.bcindex.api.service.WeightService;
import com.frobro.bcindex.api.service.persistence.*;
import com.frobro.bcindex.core.model.IndexName;
import com.frobro.bcindex.core.model.WeightApi;
import com.frobro.bcindex.core.model.WeightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

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
  public JsonData getDailyWeights(@RequestBody IndexDto dto) throws Exception {
    return cache.getResonse(dto.getIndex());
  }

  @RequestMapping(value = "/daily/weight/nocache", method = RequestMethod.POST)
  public JsonData getDailyWeightsNoCache(@RequestBody IndexDto dto) throws Exception {
    return toJsonData(dto.getIndex());
  }

  /* receive data */
  @RequestMapping(value = "blet/weight/daily", method = RequestMethod.POST)
  public void cacheUpdate(@RequestBody WeightDto dto) throws Exception {
    LOG.info("received daily weight data: " + dto.indexes.keySet());
    WeightApi data = new WeightApi(dto);

    if (data != null && data.amSecure()) {
      data.removeKeyIfExists();
      processData(data);
    }
  }

  private void processData(WeightApi data) {
    List<DoaService> doaList = dataMapper.toDoaList(data);
    updateCache(data);
    weightService.save(doaList);
  }

  private void updateCache(WeightApi data) {
    for (IndexName indexName : data.getIndexes()) {
      JsonData jsonData = cache.getResonse(indexName);
      if (jsonData != null) {
        JsonElement element = dataMapper.toJsonElement(data.getTime(),data.getWeight(indexName));
        jsonData.elementList.add(element);
      }
      else {
        // create new json data
        throw new UnsupportedOperationException("implement me");
      }
    }
  }

  private void loadCache() {
    for (IndexName indexName : IndexName.values()) {
      cache.update(toJsonData(indexName));
    }
  }

  private JsonData toJsonData(IndexName indexName) {
    return dataMapper.toJsonData(indexName, weightService.get(indexName));
  }
}
