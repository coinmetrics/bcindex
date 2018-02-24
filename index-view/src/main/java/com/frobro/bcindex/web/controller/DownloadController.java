package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.model.CsvFile;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.service.query.CsvIdBasedQuery;
import com.frobro.bcindex.web.service.query.CsvTimeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@RestController
public class DownloadController {
  private Logger log = LoggerFactory.getLogger(DownloadController.class);

  private JdbcTemplate jdbc;

  @Autowired
  public void setJdbcTemplate(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @RequestMapping(path = "/bletchley_ten.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_ten() throws IOException {
    return responseFile(IndexType.ODD_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_ten_even.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_ten_even() throws IOException {
    return responseFile(IndexType.EVEN_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_twenty.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_twenty() throws IOException {
    return responseFile(IndexType.INDEX_TWENTY.name());
  }

  @RequestMapping(path = "/bletchley_twenty_even.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_twenty_even() throws IOException {
    return responseFile(IndexType.EVEN_TWENTY.name());
  }

  @RequestMapping(path = "/bletchley_ethereum.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_eth() throws IOException {
    return responseFile(IndexType.INDEX_ETH.name());
  }

  @RequestMapping(path = "/bletchley_ethereum_even.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_eth_even() throws IOException {
    return responseFile(IndexType.EVEN_ETH.name());
  }

  @RequestMapping(path = "/bletchley_forty.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_forty() throws IOException {
    return responseFile(IndexType.FORTY_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_forty_even.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_forty_even() throws IOException {
    return responseFile(IndexType.FORTY_EVEN_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_total.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_total() throws IOException {
    return responseFile(IndexType.TOTAL_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_total_even.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_total_even() throws IOException {
    return responseFile(IndexType.TOTAL_EVEN_INDEX.name());
  }

  @RequestMapping(path = "/bletchley_currency.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_currency() throws IOException {
    return responseFile(IndexType.CURRENCY_SECTOR.name());
  }

  @RequestMapping(path = "/bletchley_platform.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_platform() throws IOException {
    return responseFile(IndexType.PLATFORM_SECTOR.name());
  }

  @RequestMapping(path = "/bletchley_application.csv", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download_application() throws IOException {
    return responseFile(IndexType.APPLICATION_SECTOR.name());
  }

  private ResponseEntity<ByteArrayResource> responseFile(String tableName) throws IOException {
    CsvFile csvFile = new CsvFile();
    // define the time interval between points
    CsvIdBasedQuery query = new CsvIdBasedQuery(jdbc);
    // get data from db
    File file = csvFile.populateAndGetFile(query.getCsvContent(tableName));

    // write data to resource
    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    ResponseEntity<ByteArrayResource> fileResponse = ResponseEntity.ok()
        .contentLength(file.length())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(resource);

    file.delete();
    return fileResponse;
  }
}

