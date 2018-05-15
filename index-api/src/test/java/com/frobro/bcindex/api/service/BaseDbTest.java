package com.frobro.bcindex.api.service;

import com.frobro.bcindex.api.service.persistence.*;
import com.frobro.bcindex.core.model.IndexName;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.frobro.bcindex.core.model.IndexName.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseDbTest {
  private Map<IndexName,CrudRepository> repos = new HashMap<>();
  private WeightTenRepo tenRepo;
  private WeightTwentyRepo twentyRepo;
  private WeightFortyRepo fortyRepo;
  private WeightTotalRepo totalRepo;
  private WeightEthRepo ethRepo;
  private WeightCurrencyRepo currRepo;
  private WeightPlatformRepo platformRepo;
  private WeightAppRepo appRepo;

  protected void setReposForTearDown(WeightTenRepo t,
                               WeightTwentyRepo tw,
                               WeightFortyRepo fo,
                               WeightTotalRepo to,
                               WeightEthRepo et,
                               WeightCurrencyRepo cu,
                               WeightPlatformRepo pl,
                               WeightAppRepo ap) {

    repos.put(TEN,t); repos.put(TWENTY, tw); repos.put(FORTY, fo); repos.put(TOTAL,to);
    repos.put(ETHEREUM, et); repos.put(CURRENCY, cu); repos.put(PLATFORM, pl); repos.put(APPLICATION, ap);
    tenRepo = t;
    twentyRepo = tw;
    fortyRepo = fo;
    totalRepo = to;
    ethRepo = et;
    currRepo = cu;
    platformRepo = pl;
    appRepo = ap;
  }

  protected WeightService newWeightService() {
    return new WeightService(tenRepo,twentyRepo,fortyRepo,totalRepo,ethRepo,currRepo,platformRepo,appRepo);
  }

  @After
  public void tearDown() {
    repos.values().stream().forEach(r -> {r.deleteAll();});
  }
}
