package com.frobro.bcindex.web.bclog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcLog4J extends BcLog {
  private static final Logger LOG = LoggerFactory.getLogger(BcLog4J.class);

  @Override
  public void debug(String msg) {
    LOG.debug(msg);
  }

  @Override
  public void info(String msg) {
    LOG.info(msg);
  }

  @Override
  public void warn(String msg) {
    LOG.warn(msg);
  }

  @Override
  public void warn(Throwable t) {
    LOG.warn(toString(t));
  }

  @Override
  public void error(String msg) {
    LOG.error(msg);
  }

  @Override
  public void error(String msg, Throwable t) {
    LOG.error(msg, t);
  }

  @Override
  public void error(Throwable t) {
    LOG.error(toString(t));
  }
}
