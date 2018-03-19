package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaWeightTotal;
import org.springframework.data.repository.CrudRepository;

public interface WeightTotalRepo extends CrudRepository<JpaWeightTotal,Long> {
}
