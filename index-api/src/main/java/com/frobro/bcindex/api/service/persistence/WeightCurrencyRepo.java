package com.frobro.bcindex.api.service.persistence;

import com.frobro.bcindex.api.domain.weight.JpaCurrency;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface WeightCurrencyRepo extends CrudRepository<JpaCurrency,Long> {
  @Transactional
  JpaCurrency findFirstByOrderByIdDesc();
}
