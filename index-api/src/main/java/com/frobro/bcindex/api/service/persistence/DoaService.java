package com.frobro.bcindex.api.service.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.frobro.bcindex.core.model.IndexName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DoaService {
  private static final Logger LOG = LoggerFactory.getLogger(DoaService.class);
  private static final String WEIGHT_KEY = "weight";
  private static final String EVEN_WEIGHT_KEY = "weightEven";
  private static final String NAME_KEY = "name";
  private static final String TIME_KEY = "time";

  private static final String KEY_VAL_DELIM = "=";
  private static final String ENTRY_DELIM = ",";
  private static final String EMPTY_JSON = "{ \"empty\": 0 }";

  private final ObjectMapper mapper = new ObjectMapper();
  private IndexName name;
  private long time;
  private Map<String,Double> dataMap;
  private Map<String,Double> dataMapEven;

  public DoaService setTime(long time) {
    this.time = time;
    return this;
  }

  public DoaService setName(IndexName name) {
    this.name = name;
    return this;
  }

  public DoaService setDataMap(Map<String,Double> dto) {
    this.dataMap = dto;
    return this;
  }

  public DoaService setDataMapEven(Map<String,Double> dto) {
    this.dataMapEven = dto;
    return this;
  }

  public Map<String,Double> getDataMap() {
    return new HashMap<>(dataMap);
  }

  public Map<String,Double> getDataMapEven() {
    return new HashMap<>(dataMapEven);
  }

  public long getTime() {
    return time;
  }

  public IndexName getName() {
    return name;
  }

  public String toApiJson() {
    validate();

    final JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode jsonNode = factory.objectNode();

    jsonNode.put(TIME_KEY, time);
    addWeight(jsonNode);
    return toJson(jsonNode);
  }

  public String toDbJson() {
    validate();

    final JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode jsonNode = factory.objectNode();

    addWeight(jsonNode);
    return toJson(jsonNode);
  }

  private void addWeight(ObjectNode node) {
    node.put(WEIGHT_KEY, toString(dataMap));
    if (name.hasEven()) {
      node.put(EVEN_WEIGHT_KEY, toString(dataMapEven));
    }
  }

  private String toJson(JsonNode node) {
    try {

      return mapper.writeValueAsString(node);

    } catch (JsonProcessingException jpe) {
      LOG.error("bad parse of weights to json for persistence",jpe);
    }
    return EMPTY_JSON;
  }

  private void validate() {
    if (dataMap == null) {
      throw new IllegalStateException(nullMapMsg("weight"));
    }

    if (name.hasEven()) {
      if (dataMapEven == null) {
        throw new IllegalStateException(nullMapMsg("even weight"));
      }
      if (dataMap.size() != dataMapEven.size()) {
        throw new IllegalStateException("weights size: " + dataMap.size()
            + " is not equal to even weights: " + dataMapEven.size());
      }
    }
  }

  private String nullMapMsg(String weightMsg) {
    return "need to set " + weightMsg + " before trying to convert them to json";
  }

  private String toString(Map<String,Double> map) {
    final StringBuilder builder = new StringBuilder();
    map.entrySet().forEach(entry -> {
      builder.append(entry.getKey()).append(KEY_VAL_DELIM)
          .append(entry.getValue()).append(ENTRY_DELIM);
    });
    return builder.toString();
  }

  private Map<String,Double> toMap(String stringMap) {
    String[] entries = stringMap.split(ENTRY_DELIM);
    Map<String,Double> map = new HashMap<>();

    for (String entry : entries) {
      String[] keyVal = entry.split(KEY_VAL_DELIM);
      map.put(keyVal[0],Double.parseDouble(keyVal[1]));
    }
    return map;
  }

  public void setData(String json) {
    JsonNode root = readJson(json);
    JsonNode weight = root.get(WEIGHT_KEY);

    setDataMap(toMap(weight.asText()));

    JsonNode evenWeight = root.get(EVEN_WEIGHT_KEY);
    if (evenWeight != null) {
      setDataMapEven(toMap(evenWeight.asText()));
    }
  }

  private JsonNode readJson(String json) {
    try {

      return mapper.readTree(json);

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DoaService that = (DoaService) o;

    if (getTime() != that.getTime()) return false;
    if (getName() != that.getName()) return false;
    if (dataMap != null ? !dataMap.equals(that.dataMap) : that.dataMap != null) return false;
    return dataMapEven != null ? dataMapEven.equals(that.dataMapEven) : that.dataMapEven == null;
  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + (int) (getTime() ^ (getTime() >>> 32));
    result = 31 * result + (dataMap != null ? dataMap.hashCode() : 0);
    result = 31 * result + (dataMapEven != null ? dataMapEven.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DoaService{" +
        "name=" + name +
        ", time=" + time +
        ", dataMap=" + dataMap +
        ", dataMapEven=" + dataMapEven +
        '}';
  }
}
