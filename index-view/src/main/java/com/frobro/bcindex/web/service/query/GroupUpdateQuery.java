package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.IndexType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupUpdateQuery {
    public static final String MAX_EXT = "_MAX";
    private static final String QUERY;

    static {
        final StringBuilder maxBid = new StringBuilder();
        final StringBuilder pxAndTime = new StringBuilder();
        final StringBuilder getMax = new StringBuilder();
        final StringBuilder newData = new StringBuilder();
        final StringBuilder query = new StringBuilder("select ");

        for (IndexType idx : IndexType.values()) {
            String name = idx.name();
            maxBid.append(name).append(MAX_EXT).append(".bid")
                .append(" as ").append(name).append(MAX_EXT)
                .append(", ");

            pxAndTime
                .append(name).append(".usd as ")
                .append(name).append("_usd, ")
                .append(name).append(".btc as ")
                .append(name).append("_btc,")
                .append(name).append(".time_stamp as ")
                .append(name).append("_time_stamp, ");

            getMax.append(" (select max(bletch_id) as bid from ")
                .append(name).append(") as ")
                .append(name).append(MAX_EXT).append(", ");

            newData.append(" (select index_value_usd as usd, ")
                .append(" index_value_btc as btc, time_stamp from ")
                .append(name).append(" order by time_stamp desc limit 1) as ")
                .append(name).append(", ");
        }
        removeLastComma(pxAndTime);
        removeLastComma(newData);

        query.append(maxBid).append(pxAndTime)
            .append(" from ")
            .append(getMax).append(newData)
            .append(";");

        QUERY = query.toString();
    }

    private static void removeLastComma(StringBuilder b) {
        b.replace(b.length() - 2, b.length() - 1, "");
    }


    public void execute(JdbcTemplate jdbc, GroupUpdate update) {
        jdbc.query(QUERY, (rs, rowNum) ->
            populate(update, rs)
        );
    }

    private GroupUpdate populate(GroupUpdate update, ResultSet rs) throws SQLException {
        for (IndexType idx : IndexType.values()) {
            String name = idx.name();
            double usd = rs.getDouble(name + "_usd");
            double btc = rs.getDouble(name + "_btc");
            long time = rs.getLong(name + "_time_stamp");
            long maxBid = rs.getLong(name + GroupUpdateQuery.MAX_EXT);

            update.update(idx, usd, btc, time, maxBid);
        }
        return update;
    }
}
