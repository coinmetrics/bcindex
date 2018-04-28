package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.model.IndexName;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.web.model.api.weight.WeightDto;
import com.frobro.bcindex.web.model.api.weight.WeightListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class WeightApiController {
  private static final Logger LOG = LoggerFactory.getLogger(WeightApiController.class);
  private WeightApi weightCache = new WeightApi();

  @RequestMapping(value = "blet/weight", method = RequestMethod.POST)
  public void cacheUpdate(@RequestBody Map<String,Map<String,Double>> data) {
    if (WeightApi.isSecure(data)) {
      weightCache.removeKeyIfExists(data);
      weightCache.update(data);
    }
  }

  @RequestMapping(value = "eight/weight", method = RequestMethod.GET)
  public Map<String,Double> getWeights() {
    return weightCache.getRawData().get(IndexName.TEN.name());
  }

  @RequestMapping(value = "api/weight", method = RequestMethod.POST)
  public Map<String,Double> getApiWeights(@RequestBody WeightDto dto) {
    Map<String,Double> response;

    try {

      response = weightCache.getWeight(dto.getIndex());

    } catch (Exception e) {
      LOG.error("could not get weight data ",e);
      response = Collections.EMPTY_MAP;
    }

    return response;
  }

  @RequestMapping(value = "api/weight/list", method = RequestMethod.POST)
  public Map<String,Map<String,Double>> getApiWeights(@RequestBody WeightListDto dto) {
    Map<String,Map<String,Double>> response;

    try {

      response =  weightCache.getWeight(dto.getIndexList());

    } catch (Exception e) {
      LOG.error("could not get weight data ",e);
      response = Collections.EMPTY_MAP;
    }

    return response;
  }
}
