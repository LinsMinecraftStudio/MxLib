package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;

public class DeleteSQL extends SQL {
    private String table;
    private Condition whereCondition;

    DeleteSQL() {}

    public DeleteSQL from(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public DeleteSQL where(Condition condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    String getSql() {
        sqlBuilder.setLength(0);
        sqlBuilder.append("DELETE FROM ").append(table);

        if (whereCondition != null) {
            sqlBuilder.append(" WHERE ").append(whereCondition.getSql());
            parameters.addAll(whereCondition.getParameters());
        }

        return sqlBuilder.toString();
    }
}
