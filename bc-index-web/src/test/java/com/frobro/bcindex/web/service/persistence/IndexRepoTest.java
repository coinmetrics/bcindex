package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;
import com.frobro.bcindex.web.domain.JpaIndex;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.config.RepositoryConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by rise on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RepositoryConfiguration.class})
public class IndexRepoTest {

  private IndexRepo indexRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo) {
    this.indexRepo = repo;
  }

  @Ignore // not complete. 'no qualifying bean'
  @Test
  public void testSave() {
    JpaIndex index = new JpaIndex(12,34);
    index.setId(1234567);

    indexRepo.save(index);
    JpaIndex retreived = indexRepo.findOne(index.getId());

    assertEquals(index.getUsdPerBtc(), retreived.getUsdPerBtc(), 0.01);
    assertEquals(index.getIndexValueUsd(), retreived.getIndexValueUsd(), 0.01);
  }
}
