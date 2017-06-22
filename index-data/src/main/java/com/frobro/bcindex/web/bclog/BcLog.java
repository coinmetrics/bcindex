package com.frobro.bcindex.web.bclog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rise on 3/23/17.
 */
public class BcLog {

  private static final String LOG_KEY = "bclog.console";
  private Logger log;
  private boolean useConsole;
  private final String className;

  public static BcLog getLogger(Class clz) {
    return new BcLog(clz);
  }

  public static String getLogKey() {
    return LOG_KEY;
  }

  private BcLog(Class clz) {
    this.className = createClassString(clz);
    log =  LoggerFactory.getLogger(clz);
    useConsole = System.getProperties().containsKey(LOG_KEY);
  }

  public void info(String msg) {
    log(LogLevel.INFO, msg);
  }

  public void error(String msg) {
    log(LogLevel.ERROR, msg);
  }

  public void error(String msg, Throwable t) {
    error(msg + "\n" + toString(t));
  }

  public void error(Throwable t) {
    error(toString(t));
  }

  public void debug(String msg) {
    log(LogLevel.DEBUG, msg);
  }

  private void log(LogLevel level, String msg) {
    if (useConsole) {
      System.out.println("["+ level + "]" + " " + className + " " + msg);
    }
    else {
      if (level == LogLevel.INFO) {
        log.info(msg);
      }
      else if (level == LogLevel.DEBUG) {
        log.debug(msg);
      }
    }
  }

  private String createClassString(Class clz) {
    return clz.getName();
  }

  private String toString(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }

  private enum LogLevel {
    INFO, DEBUG, WARN, ERROR
  }
}
