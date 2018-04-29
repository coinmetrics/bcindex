package com.frobro.bcindex.api.service.weight;

import com.frobro.bcindex.api.domain.weight.JpaWeightTwenty;
import org.springframework.data.repository.CrudRepository;

public interface WeightTwentyRepo extends CrudRepository<JpaWeightTwenty,Long> {
}
