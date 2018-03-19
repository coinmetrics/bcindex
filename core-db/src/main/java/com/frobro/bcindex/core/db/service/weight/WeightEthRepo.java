package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaWeightEther;
import org.springframework.data.repository.CrudRepository;

public interface WeightEthRepo extends CrudRepository<JpaWeightEther,Long> {
}
