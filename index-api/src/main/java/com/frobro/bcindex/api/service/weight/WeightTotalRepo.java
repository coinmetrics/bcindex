package com.frobro.bcindex.api.service.weight;

import com.frobro.bcindex.api.domain.weight.JpaWeightTotal;
import org.springframework.data.repository.CrudRepository;

public interface WeightTotalRepo extends CrudRepository<JpaWeightTotal,Long> {
}
