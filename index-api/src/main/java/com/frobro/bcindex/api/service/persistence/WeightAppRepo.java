package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeightApp;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightAppRepo extends CrudRepository<JpaWeightApp,Long> {
  @Transactional
  JpaWeightApp findFirstByOrderByIdDesc();
}
