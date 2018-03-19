package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaPlatform;
import org.springframework.data.repository.CrudRepository;

public interface WeightPlatformRepo extends CrudRepository<JpaPlatform,Long> {
}
