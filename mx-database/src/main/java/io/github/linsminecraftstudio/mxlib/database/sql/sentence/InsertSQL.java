package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertSQL extends SQL {
    private final Map<String, Object> values = new LinkedHashMap<>();
    private final boolean upsert;

    private String table;

    InsertSQL(boolean upsert) {
        this.upsert = upsert;
    }

    public InsertSQL into(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public InsertSQL value(String column, Object value) {
        validateIdentifier(column);
        values.put(column, value);
        return this;
    }

    @Override
    String getSql() {
        sqlBuilder.setLength(0);
        sqlBuilder.append("INSERT INTO ").append(table);

        if (!values.isEmpty()) {
            sqlBuilder.append(" (")
                    .append(String.join(", ", values.keySet()))
                    .append(") VALUES (")
                    .append(String.join(", ", Collections.nCopies(values.size(), "?")))
                    .append(")");
            parameters.addAll(values.values());
        }

        if (upsert) {
            sqlBuilder.append(" ON DUPLICATE KEY UPDATE");
        }

        return sqlBuilder.toString();
    }
}
