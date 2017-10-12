package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.IndexType;
import org.springframework.jdbc.core.JdbcTemplate;

class LastUpdateQuery {

  public GroupUpdate getLatestFromDb(JdbcTemplate jdbc) {
    GroupUpdate update = new GroupUpdate();

    jdbc.query(getQuery(), (rs, rowNum) ->
        update.hashCode() // replace with update calls
    );

    return null;
  }

  private String getQuery() {
    return "select * from" +
        "(select index_value_usd as ten_usd, index_value_btc as ten_btc, time_stamp as ten_time "
        + " from " + IndexType.ODD_INDEX.name() + " order by time_stamp desc limit 1) as ten," +

        "(select index_value_usd as ten_even_usd, index_value_btc as ten_even_btc, time_stamp as ten_even_time "
        + " from " + IndexType.ODD_INDEX.name() + " even_index order by time_stamp desc limit 1) as ten_even," +

        "(select index_value_usd as twenty_usd, index_value_btc as twenty_btc, time_stamp as twenty_time "
        + " from " + IndexType.ODD_INDEX.name() + " index_twenty order by time_stamp desc limit 1) as twenty," +

        "(select index_value_usd as twenty_even_usd, index_value_btc as twenty_even_btc, time_stamp as twenty_even_time "
        + " from " + IndexType.ODD_INDEX.name() + " even_twenty order by time_stamp desc limit 1) as twenty_even," +

        "(select index_value_usd as eth_usd, index_value_btc as eth_btc, time_stamp as eth_time "
        + " from " + IndexType.ODD_INDEX.name() + "from index_eth order by time_stamp desc limit 1) as eth," +

        "(select index_value_usd as eth_even_usd, index_value_btc as eth_even_btc, time_stamp as eth_even_time "
        + " from " + IndexType.ODD_INDEX.name() + " even_eth order by time_stamp desc limit 1) as eth_even;";
  }
}