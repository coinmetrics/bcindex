package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaPlatform;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightPlatformRepo extends CrudRepository<JpaPlatform,Long> {
  @Transactional
  JpaPlatform findFirstByOrderByIdDesc();
}
