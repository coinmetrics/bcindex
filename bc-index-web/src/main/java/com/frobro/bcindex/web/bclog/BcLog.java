package com.frobro.bcindex.web.bclog;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rise on 3/23/17.
 */
public class BcLog {

  private static final String INFO  = "INFO";
  private static final String DEBUG = "DEBUG";
  private static final String WARN  = "WARN";
  private static final String ERROR = "ERROR";

  private final String className;

  public static BcLog getLogger(Class clz) {
    return new BcLog(clz);
  }

  private BcLog(Class clz) {
    this.className = createClassString(clz);
  }

  public void info(String msg) {
    log(INFO, msg);
  }

  public void error(String msg) {
    log(ERROR, msg);
  }

  public void error(String msg, Throwable t) {
    error(msg + "\n" + toString(t));
  }

  public void error(Throwable t) {
    error(toString(t));
  }

  public void debug(String msg) {
    log(DEBUG, msg);
  }

  private void log(String level, String msg) {
    System.out.println("["+ level + "]" + " " + className + " " + msg);
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
}
