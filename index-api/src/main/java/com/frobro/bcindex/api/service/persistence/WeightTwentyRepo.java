package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeightTwenty;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightTwentyRepo extends CrudRepository<JpaWeightTwenty,Long> {
  @Transactional
  JpaWeightTwenty findFirstByOrderByIdDesc();
}