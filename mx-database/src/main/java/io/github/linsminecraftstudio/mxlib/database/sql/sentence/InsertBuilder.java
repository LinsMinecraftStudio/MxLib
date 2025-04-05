package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertBuilder extends AbstractSqlBuilder {
    private final Map<String, Object> values = new LinkedHashMap<>();
    private String table;

    public InsertBuilder into(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public InsertBuilder value(String column, Object value) {
        validateIdentifier(column);
        values.put(column, value);
        return this;
    }

    @Override
    public String getSql() {
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

        return sqlBuilder.toString();
    }
}
