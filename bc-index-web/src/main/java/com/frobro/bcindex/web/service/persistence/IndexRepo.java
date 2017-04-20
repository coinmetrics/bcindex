package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaIndex;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rise on 4/19/17.
 */
public interface IndexRepo extends JpaRepository<JpaIndex, Long> {
}
