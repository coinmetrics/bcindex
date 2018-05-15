package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaPlatform;
import org.springframework.data.repository.CrudRepository;

public interface WeightPlatformRepo extends CrudRepository<JpaPlatform,Long> {
  JpaPlatform findFirstByOrderByIdDesc();
}
