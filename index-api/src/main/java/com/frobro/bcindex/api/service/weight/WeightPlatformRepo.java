package com.frobro.bcindex.api.service.weight;

import com.frobro.bcindex.api.domain.weight.JpaPlatform;
import org.springframework.data.repository.CrudRepository;

public interface WeightPlatformRepo extends CrudRepository<JpaPlatform,Long> {
}
