package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaWeightTen;
import org.springframework.data.repository.CrudRepository;

public interface WeightTenRepo extends CrudRepository<JpaWeightTen,Long> {
  JpaWeightTen findFirstByOrderByIdDesc();
}
