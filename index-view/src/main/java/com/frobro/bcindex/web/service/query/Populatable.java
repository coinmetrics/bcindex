package com.frobro.bcindex.web.service.query;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Populatable {
  Populatable populate(ResultSet result, int size) throws SQLException;
}

