package com.frobro.bcindex.web.service;

import java.util.HashSet;
import java.util.Set;

public class ApiKeyService {
  private static final String NOMICS_KEY = "abcnomics";
  private static final String PROP_SHOT_KEY = "abctrader";
  private static final Set<String> knownKeys = new HashSet<>();

  static {
    knownKeys.add(NOMICS_KEY);
    knownKeys.add(PROP_SHOT_KEY);
  }

  public static boolean isKeyValid(String apiKey) {
    return knownKeys.contains(apiKey);
  }

  public static String getUnknowKeyMsg() {
    return "The key you entered is not valid";
  }
}
