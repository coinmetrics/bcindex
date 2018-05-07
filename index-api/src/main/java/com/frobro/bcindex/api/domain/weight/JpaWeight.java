package com.frobro.bcindex.api.domain.weight;

import com.frobro.bcindex.api.service.persistence.DoaService;
import com.frobro.bcindex.core.db.model.IndexName;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class JpaWeight {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private long timeStamp;
  private long bletchId;
  private String weights;
  private String indexName;

  public JpaWeight() {}

  public void populate(DoaService doa) {
    indexName = doa.getName().name();
    timeStamp = doa.getTime();
    weights = doa.toDbJson();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getBletchId() {
    return bletchId;
  }

  public void setBletchId(long bletchId) {
    this.bletchId = bletchId;
  }

  public String getWeights() {
    return weights;
  }

  public void setWeights(String weights) {
    this.weights = weights;
  }

  public String getIndexName() {
    return indexName;
  }

  public JpaWeight setIndexName(String indexName) {
    this.indexName = indexName;
    return this;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public JpaWeight setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
    return this;
  }

  public DoaService getDoa() {
    DoaService doa = new DoaService();
    doa.setTime(timeStamp).setName(IndexName.getIndex(indexName));
    doa.setData(weights);
    return doa;
  }
}
