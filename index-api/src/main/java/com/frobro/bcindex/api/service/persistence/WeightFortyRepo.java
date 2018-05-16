package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeightForty;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightFortyRepo extends CrudRepository<JpaWeightForty,Long> {
  @Transactional
  JpaWeightForty findFirstByOrderByIdDesc();
}
