package com.frobro.bcindex.web.bclog;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ConsoleLogger extends BcLog {
  private final String className;

  protected ConsoleLogger(Class clz) {
    super();
    this.className = clz.getName();
  }

  public void info(String msg) {
    log(LogLevel.INFO, msg);
  }

 public void warn(String msg) {
   log(LogLevel.WARN, msg);
 }

 public void warn(Throwable t) {
    warn(toString(t));
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
    System.out.println("["+ level + "]" + " " + className + " " + msg);
  }

  private String createClassString(Class clz) {
    return clz.getName();
  }

  private enum LogLevel {
    INFO, DEBUG, WARN, ERROR
  }
}
