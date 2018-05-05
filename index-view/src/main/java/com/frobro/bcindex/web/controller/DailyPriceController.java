package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.service.JsonIdBasedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DailyPriceController {

  private JdbcTemplate jdbc;

  @Autowired
  public void setJdbcTemplate(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @RequestMapping(path = "/daily/ten", method = RequestMethod.GET)
  public String download_ten() throws IOException {
    return getDataAsJson(IndexType.ODD_INDEX);
  }

  @RequestMapping(path = "/daily/ten/even", method = RequestMethod.GET)
  public String download_ten_even() throws IOException {
    return getDataAsJson(IndexType.EVEN_INDEX);
  }

  @RequestMapping(path = "/daily/twenty", method = RequestMethod.GET)
  public String download_twenty() throws IOException {
    return getDataAsJson(IndexType.INDEX_TWENTY);
  }

  @RequestMapping(path = "/daily/twenty/even", method = RequestMethod.GET)
  public String download_twenty_even() throws IOException {
    return getDataAsJson(IndexType.EVEN_TWENTY);
  }

  @RequestMapping(path = "/daily/forty", method = RequestMethod.GET)
  public String download_forty() throws IOException {
    return getDataAsJson(IndexType.FORTY_INDEX);
  }

  @RequestMapping(path = "/daily/forty/even", method = RequestMethod.GET)
  public String download_forty_even() throws IOException {
    return getDataAsJson(IndexType.FORTY_EVEN_INDEX);
  }

  @RequestMapping(path = "/daily/total", method = RequestMethod.GET)
  public String download_total() throws IOException {
    return getDataAsJson(IndexType.TOTAL_INDEX);
  }

  @RequestMapping(path = "/daily/total/even", method = RequestMethod.GET)
  public String download_total_even() throws IOException {
    return getDataAsJson(IndexType.TOTAL_EVEN_INDEX);
  }

  @RequestMapping(path = "/daily/ethereum", method = RequestMethod.GET)
  public String download_ethereum() throws IOException {
    return getDataAsJson(IndexType.INDEX_ETH);
  }

  @RequestMapping(path = "/daily/ethereum/even", method = RequestMethod.GET)
  public String download_ethereum_even() throws IOException {
    return getDataAsJson(IndexType.EVEN_ETH);
  }

  @RequestMapping(path = "/daily/currency", method = RequestMethod.GET)
  public String download_currency() throws IOException {
    return getDataAsJson(IndexType.CURRENCY_SECTOR);
  }

  @RequestMapping(path = "/daily/platform", method = RequestMethod.GET)
  public String download_platform() throws IOException {
    return getDataAsJson(IndexType.PLATFORM_SECTOR);
  }

  @RequestMapping(path = "/daily/application", method = RequestMethod.GET)
  public String download_application() throws IOException {
    return getDataAsJson(IndexType.APPLICATION_SECTOR);
  }

  private String getDataAsJson(IndexType index) {
    JsonIdBasedQuery query = new JsonIdBasedQuery(jdbc);
    return query.getDbData(index);
  }
}
