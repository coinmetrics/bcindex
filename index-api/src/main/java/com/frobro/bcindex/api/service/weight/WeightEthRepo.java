package com.frobro.bcindex.api.service.weight;

import com.frobro.bcindex.api.domain.weight.JpaWeightEther;
import org.springframework.data.repository.CrudRepository;

public interface WeightEthRepo extends CrudRepository<JpaWeightEther,Long> {
}
