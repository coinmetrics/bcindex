package com.frobro.bcindex.web.service;

/**
 * Created by rise on 6/13/17.
 */
public class QueryService {
  public static final String TEN_IDX_BTC = "ten_btc";
  public static final String TEN_IDX_USD = "ten_usd";
  public static final String TEN_IDX_EVEN_BTC = "ten_ev_btc";
  public static final String TEN_IDX_EVEN_USD = "ten_ev_usd";
  public static final String TWENTY_IDX_BTC = "twe_btc";
  public static final String TWENTY_IDX_USD = "twe_usd";
  public static final String TWENTY_IDX_EVEN_BTC = "twe_ev_btc";
  public static final String TWENTY_IDX_EVEN_USD = "twe_ev_usd";

  public static final String QUERY_LATEST = "select " +
      "  tn.index_value_btc as " + TEN_IDX_BTC + ", " +
      "  tn.index_value_usd  as " + TEN_IDX_USD + ", " +
      "  tne.index_value_btc as " + TEN_IDX_EVEN_BTC + "," +
      "  tne.index_value_usd as " + TEN_IDX_EVEN_USD + ", " +
      "  twi.index_value_btc  as " + TWENTY_IDX_BTC + "," +
      "  twi.index_value_usd as " + TWENTY_IDX_USD + ", " +
      "  twe.index_value_btc as " + TWENTY_IDX_EVEN_BTC + "," +
      "  twe.index_value_usd as " + TWENTY_IDX_EVEN_USD + " " +
      "from " +
      "  (select index_value_btc,index_value_usd from odd_index where id > (select count(*) from odd_index)-1 order by id desc) as tn," +
      "  (select index_value_btc,index_value_usd from even_index where id > (select count(*) from odd_index)-1 order by id desc) as tne," +
      "  (select index_value_btc,index_value_usd from even_index where id > (select count(*) from odd_index)-1 order by id desc) as twi," +
      "  (select index_value_btc,index_value_usd from even_index where id > (select count(*) from odd_index)-1 order by id desc) as twe;";
}
