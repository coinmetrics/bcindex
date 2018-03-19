package com.frobro.bcindex.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.model.IndexName;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.web.model.api.weight.WeightDto;
import com.frobro.bcindex.web.model.api.weight.WeightListDto;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WeightApiController {
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
    return weightCache.getWeight(dto.getIndex());
  }

  @RequestMapping(value = "api/weight/list", method = RequestMethod.POST)
  public Map<String,Map<String,Double>> getApiWeights(@RequestBody WeightListDto dto) {
    return weightCache.getWeight(dto.getIndexList());
  }

}
