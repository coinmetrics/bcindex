package com.frobro.bcindex.api.service.weight;

import com.frobro.bcindex.api.domain.weight.JpaCurrency;
import org.springframework.data.repository.CrudRepository;

public interface WeightCurrencyRepo extends CrudRepository<JpaCurrency,Long> {
}
