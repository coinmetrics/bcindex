package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.service.time.TimeService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvFile {

  private static final String LINE_BREAK = "\n";
  private static final String FILE_NAME = "Csv" + TimeService.currentTimeMillis() + ".csv";
  private final File file;

  public CsvFile() {
    this.file = new File(FILE_NAME);
  }

  public File populateAndGetFile(List<String> lines) throws IOException {
    FileWriter writer = new FileWriter(file);

    // write data to file
    for (String line : lines) {
      writer.write(line + LINE_BREAK);
    }
    writer.close();

    return file;
  }
}
