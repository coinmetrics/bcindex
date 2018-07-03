package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeightTotal;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightTotalRepo extends CrudRepository<JpaWeightTotal,Long> {
  @Transactional
  JpaWeightTotal findFirstByOrderByIdDesc();
}