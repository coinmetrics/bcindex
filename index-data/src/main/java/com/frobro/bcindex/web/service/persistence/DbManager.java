package com.frobro.bcindex.web.service.persistence;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by rise on 5/19/17.
 */
public class DbManager {

  private static final String TCP_PORT = "9123";
  private static final String WEB_PORT = "9122";

  private Server tcpServer;
  private Server h2Console;

  public DbManager() {
    startTcpServer();
  }

  public DbManager startDb() {
    try {

      tcpServer.start();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public DbManager startH2Console() {
    try {

      h2Console = Server.createWebServer("-webPort", WEB_PORT, "-webAllowOthers")
          .start();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  private void startTcpServer() {
    try {

      tcpServer = Server.createTcpServer("-tcpPort", TCP_PORT, "-tcpAllowOthers");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
