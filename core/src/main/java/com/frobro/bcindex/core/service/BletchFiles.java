package com.frobro.bcindex.core.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BletchFiles {

  public static List<String> linesToList(String fileName) {
    InputStream data = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    return readLines(new BufferedReader(new InputStreamReader(data, StandardCharsets.UTF_8)));
  }

  private static List<String> readLines(BufferedReader data) {
    return data.lines().collect(Collectors.toList());
  }

  public static List<String> fileSystemToList(String fileName) {
    return readLines(getBufferedReader(Paths.get(fileName)));
  }

  private static BufferedReader getBufferedReader(Path path) {
    try {

      return Files.newBufferedReader(path);

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
