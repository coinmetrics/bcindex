package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.model.JsonData;
import com.frobro.bcindex.api.model.JsonElement;
import com.frobro.bcindex.api.service.persistence.DoaService;
import com.frobro.bcindex.core.model.IndexName;
import com.frobro.bcindex.core.model.WeightApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DataMapper {
  private static final Logger LOG = LoggerFactory.getLogger(DataMapper.class);

  public List<DoaService> toDoaList(WeightApi data) {
    // weight api to doa service
    long time = data.getTime();

    List<DoaService> doaList = new LinkedList<>();
    for (IndexName indexName : data.getIndexes()) {
      // if even skip
      if (not(indexName.isEven())) {
        DoaService doa = new DoaService();
        // add data
        doa.setDataMap(data.getWeight(indexName));
        doa.setTime(time);
        doa.setName(indexName);

        // check for even
        IndexName even = indexName.getEvenMatch();
        // if has associated even index add it
        if (even != null) {

          if (data.getIndexes().contains(even)) {
            doa.setDataMapEven(data.getWeight(even));
          }
          else {
            LOG.error("could not find even index: " + even
                + ". Matching index: " + indexName + ". In data: " + data.getIndexes());
          }

        }
        doaList.add(doa);
      }
    }
    return doaList;
  }

  public JsonElement toJsonElement(long time, Map<String,Double> weights) {
    JsonElement element = new JsonElement();
    element.time = time;
    element.dataMap = weights;
    return element;
  }

  private boolean not(boolean b) {
    return !b;
  }

  public JsonData toJsonData(IndexName indexName, List<DoaService> doaList) {
    JsonData jsonData = new JsonData();
    List<JsonElement> jElementList = toJElementList(indexName.isEven(), doaList);
    jsonData.elementList = jElementList;
    jsonData.index = indexName;
    return jsonData;
  }

  // get even or normal weight depending on isEven
  public List<JsonElement> toJElementList(boolean isEven, List<DoaService> doaList) {
    List<JsonElement> jsonElementList = new ArrayList<>(doaList.size());

    for (DoaService doa : doaList) {
      JsonElement jData = new JsonElement();
      jData.time = doa.getTime();

      if (isEven) {
        jData.dataMap = doa.getDataMapEven();
      }
      else {
        jData.dataMap = doa.getDataMap();
      }
      jsonElementList.add(jData);
    }
    Collections.sort(jsonElementList);
    return jsonElementList;
  }
}
