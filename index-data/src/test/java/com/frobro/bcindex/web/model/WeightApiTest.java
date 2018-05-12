package com.frobro.bcindex.web.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.model.IndexName;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.core.db.model.WeightDto;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class WeightApiTest {
  private int totalDataSize = 1;

  @Test
  public void testSerialization() throws Exception {
    // given
    IndexName indexA = IndexName.APPLICATION;
    IndexName indexB = IndexName.FORTY;
    // and
    Map<String,Double> dataA = createData();
    Map<String,Double> dataB = createData();
    // and
    long time = System.currentTimeMillis();
    // and
    Map<IndexName,Map<String,Double>> weights = new HashMap<>();
    weights.put(indexA,dataA);
    weights.put(indexB,dataB);
    // and
    WeightApi api = new WeightApi();
    api.update(time,weights);


    // when data is serialized
    String json = new ObjectMapper().writeValueAsString(api.getWeightDto());

    // and data is deserialized
    WeightApi parsedApi = new WeightApi(new ObjectMapper().readValue(json, WeightDto.class));

    // then the original and deserialized data are equal
    assertEquals(api,parsedApi);
  }

  private Map<String,Double> createData() {
    int i = totalDataSize;
    int size = totalDataSize + 10;
    totalDataSize = size;

    Map<String,Double> data = new HashMap<>();

    int minNum = 0;
    int maxNum = 100001; // upper bound is exclusive --> want 100000

    for (; i<size; i++) {
      Double randomNum = ThreadLocalRandom.current().nextDouble(minNum, maxNum);
      String key = "symbol_" + i;

      data.put(key,randomNum);
    }
    return data;
  }
}
