package com.frobro.bcindex.core.db.service.weight;

import com.frobro.bcindex.core.db.domain.weight.JpaCurrency;
import org.springframework.data.repository.CrudRepository;

public interface WeightCurrencyRepo extends CrudRepository<JpaCurrency,Long> {
}
