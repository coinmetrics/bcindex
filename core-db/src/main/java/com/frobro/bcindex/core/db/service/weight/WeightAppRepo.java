package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaWeightApp;
import org.springframework.data.repository.CrudRepository;

public interface WeightAppRepo extends CrudRepository<JpaWeightApp,Long> {
}
