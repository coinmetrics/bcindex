package com.frobro.bcindex.web.bclog;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rise on 3/23/17.
 */
abstract public class BcLog {
  private static final String LOG_KEY = "bclog.console";

  public static BcLog getLogger(Class clz) {
    if (System.getProperties().containsKey(LOG_KEY)) {
      return new ConsoleLogger(clz);
    }
    return new BcLog4J();
  }

  public static String getLogKey() {
    return LOG_KEY;
  }

  protected BcLog() {
  }

  abstract public void info(String msg);

  abstract public void warn(String msg);

  abstract public void warn(Throwable t);

  abstract public void error(String msg);

  abstract public void error(String msg, Throwable t);

  abstract public void error(Throwable t);

  abstract public void debug(String msg);

  protected String toString(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }
}
