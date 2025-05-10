package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateSQL extends SQL {
    private final Map<String, Object> updates = new LinkedHashMap<>();

    private String table;
    private Condition whereCondition;

    UpdateSQL() {}

    public UpdateSQL table(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public UpdateSQL set(String column, Object value) {
        validateIdentifier(column);
        updates.put(column, value);
        return this;
    }

    public UpdateSQL where(Condition condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    public String getSql(DatabaseType type) {
        sqlBuilder.setLength(0);
        sqlBuilder.append("UPDATE ").append(table).append(" SET ");

        List<String> setClauses = new ArrayList<>();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            setClauses.add(entry.getKey() + " = ?");
            parameters.add(entry.getValue());
        }
        sqlBuilder.append(String.join(", ", setClauses));

        if (whereCondition != null) {
            sqlBuilder.append(" WHERE ").append(whereCondition.getSql());
            parameters.addAll(whereCondition.getParameters());
        }

        return sqlBuilder.toString();
    }
}
