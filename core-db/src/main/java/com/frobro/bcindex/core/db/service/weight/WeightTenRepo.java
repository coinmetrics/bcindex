package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaWeightTen;
import org.springframework.data.repository.CrudRepository;

public interface WeightTenRepo extends CrudRepository<JpaWeightTen,Long> {
}
