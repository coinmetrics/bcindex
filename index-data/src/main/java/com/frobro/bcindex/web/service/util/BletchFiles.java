package com.frobro.bcindex.web.service.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rise on 6/17/17.
 */
public class BletchFiles {

  public static List<String> linesToList(String fileName) {
    InputStream data = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    return readLines(data);
  }

  private static List<String> readLines(InputStream data) {
    return new BufferedReader(new InputStreamReader(data,
        StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
  }
}
