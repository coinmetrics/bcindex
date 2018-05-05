package com.frobro.bcindex.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.service.query.IdBasedQuery;
import com.frobro.bcindex.web.service.query.JsonHolder;
import com.frobro.bcindex.web.service.query.JsonRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JsonIdBasedQuery extends IdBasedQuery {
  private static final BcLog LOG = BcLog.getLogger(JsonIdBasedQuery.class);

  public JsonIdBasedQuery(JdbcTemplate jdbc) {
    super(jdbc);
  }

  @Override
  public String query(String table) {
    // TODO: hard coded Postgres functions below (to_timestamp, date_part)
    return "select index_value_usd as " + PX_USD +
                ", index_value_btc as " + PX_BTC +
                ", time_stamp as " + EPOCH_TIME +
        "   from " + table + " " +
        "   where date_part('hour',to_timestamp(time_stamp/1000)) = 0 " +
        "   and date_part('minute',to_timestamp(time_stamp/1000)) = 0 order by bletch_id desc";
  }

  public String getDbData(IndexType index) {
    String query = query(index.name());

    JsonRowMapper rowMapper = new JsonRowMapper();

    jdbc.query(query, rowMapper);
    return toString(rowMapper.holder.build());
  }

  private String toString(List<JsonRow> rows) {
    String json;
    try {

      json = new ObjectMapper().writeValueAsString(rows);

    } catch (JsonProcessingException jpe) {
      LOG.error("could not parse data to json string",jpe);
      json = "";
    }

    return json;
  }

  private class JsonRowMapper implements RowMapper<Object> {
    JsonHolder holder = new JsonHolder();

    @Override
    public Object mapRow(ResultSet rs, int num) {
      try {
        JsonRow json = new JsonRow();
        json.pxBtc = rs.getString(PX_BTC);
        json.pxUsd = rs.getString(PX_USD);
        json.time = rs.getString(EPOCH_TIME);

        holder.body(json);

      } catch (SQLException sqle) {
        LOG.error("could not data from db",sqle);
        return new JsonHolder();
      }
      return holder;
    }
  }
}
