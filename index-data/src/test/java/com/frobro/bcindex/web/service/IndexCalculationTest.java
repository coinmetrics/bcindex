package com.frobro.bcindex.web.service;

import static org.junit.Assert.assertEquals;

import com.frobro.bcindex.web.model.BletchInEth;
import com.frobro.bcindex.web.model.BletchInTen;
import com.frobro.bcindex.web.model.BletchInTwenty;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by rise on 7/15/17.
 */
public class IndexCalculationTest {

  @Ignore // just recalc the vals to unignore
  @Test
  public void testTen() {
    // given
    BletchleyData inputData = new BletchInTen();
    inputData.setLastUsdBtc(getLastBtc());
    inputData.setLastUpdate(getTime());
    // and
    IndexCalculator calculator = new IndexCalculatorTen();
    calculator.updateLast(inputData);

    // when
    IndexDbDto dto = calculator.calcuateOddIndex();
    IndexDbDto evenDto = calculator.calculateEvenIndex();

    // check ten index
    isEqual(getTenBtc(), dto.indexValueBtc);
    isEqual(getUsd(getTenBtc()), dto.indexValueUsd);
    assertEquals(getTime(), dto.timeStamp);

    // check ten even
    isEqual(evenTenBtc(), evenDto.indexValueBtc);
    isEqual(getUsd(evenTenBtc()), evenDto.indexValueUsd);
    assertEquals(getTime(), evenDto.timeStamp);
  }

  // need to perform the manual
  // calculation for the expectations
  @Ignore
  @Test
  public void testEth() {
    System.setProperty("bclog.console", "true");
    // given
    BletchleyData inputData = new BletchInEth();
//    BletchleyData inputData = new BletchInTwenty();

    inputData.setLastUsdBtc(getLastBtc());
    inputData.setLastUpdate(getTime());
    // and
    IndexCalculator calculator = new IndexCalculatorEth();
//    IndexCalculator calculator = new IndexCalculatorTwenty();
    calculator.updateLast(inputData);

    // when
    IndexDbDto dto = calculator.calcuateOddIndex();
    IndexDbDto evenDto = calculator.calculateEvenIndex();

    // check ten index
    isEqual(getTenBtc(), dto.indexValueBtc);
    isEqual(getUsd(getTenBtc()), dto.indexValueUsd);
    assertEquals(getTime(), dto.timeStamp);

    // check ten even
    isEqual(evenTenBtc(), evenDto.indexValueBtc);
    isEqual(getUsd(evenTenBtc()), evenDto.indexValueUsd);
    assertEquals(getTime(), evenDto.timeStamp);
  }

  private void isEqual(double expect, double actual) {
    assertEquals(expect, actual,  0.001);
  }

  private double getTenBtc() {
    return 0.13943830969782742;
  }

  private double getUsd(double idxBtc) {
    return idxBtc * getLastBtc();
  }

  private double evenTenBtc() {
    return 0.297351822228;
  }

  private long getTime() {
    return 1500142719706L;
  }

  private double getLastBtc() {
    return 2015.57;
  }

  private String getApiJsonEth() {
    return fileToString("eth_api.json");
  }

  private String getApiJsonTen() {
    return fileToString("ten_api.json");
  }

  private String fileToString(String fileName) {
    fileName = "src/test/resources/test_data" + File.separator + fileName;
    try {

      return new String(Files.readAllBytes(Paths.get(fileName)));

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
