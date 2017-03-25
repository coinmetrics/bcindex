package com.frobro.bcindex.web.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by rise on 3/23/17.
 */
public class BusinessRules {

  private static final String MKT_CAP_FILE = "business_rules/mkt_cap.txt";
  private static final String DELIMINATOR = "=";

  public Map<String,Double> getMarketCapRules() {
    Properties prop = new Properties();
    InputStream input = null;

    Map<String,Double> nameToMultiplier = new HashMap<>();
    try {

      input = Thread.currentThread().getContextClassLoader().getResourceAsStream(MKT_CAP_FILE);
      prop.load(input);

      prop.entrySet().stream().forEach(entry -> {
        String[] vals = entry.toString().split(DELIMINATOR);

        if (vals.length != 2) {
          throw new RuntimeException("could not parse the property: " + entry);
        }

        nameToMultiplier.put(String.valueOf(entry.getKey()),
            Double.parseDouble(String.valueOf(entry.getValue())));
      });


    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return nameToMultiplier;
  }
}
