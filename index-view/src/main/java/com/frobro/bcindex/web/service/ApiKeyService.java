package com.frobro.bcindex.web.service;

import java.util.HashSet;
import java.util.Set;

public class ApiKeyService {
  private static Set<String> knownKeys = new HashSet<>();

  static {
    knownKeys.add("fkdjfdkfj98r9");
  }

  public static boolean isKeyValid(String apiKey) {
    return knownKeys.contains(apiKey);
  }

  public static String getUnknowKeyMsg() {
    return "The key you entered is not valid";
  }
}
