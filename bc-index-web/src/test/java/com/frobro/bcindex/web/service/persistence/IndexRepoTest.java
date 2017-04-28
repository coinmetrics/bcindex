package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;
import com.frobro.bcindex.web.domain.JpaIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by rise on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexRepoTest {

  private IndexRepo indexRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo) {
    this.indexRepo = repo;
  }

  @Test
  public void testSave() {
    JpaIndex index = JpaIndex.create()
        .setIndexValueBtc(12)
        .setEvenIndexValueUsd(34)
        .setId(1234567L);

    indexRepo.save(index);
    JpaIndex retreived = indexRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    assertEquals(index.getIndexValueBtc(), retreived.getIndexValueBtc(), 0.01);
    assertEquals(index.getIndexValueUsd(), retreived.getIndexValueUsd(), 0.01);
  }
}
