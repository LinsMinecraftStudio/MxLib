package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;

public class DeleteBuilder extends AbstractSqlBuilder {
    private String table;
    private Condition whereCondition;

    public DeleteBuilder from(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public DeleteBuilder where(Condition condition) {
        this.whereCondition = condition;
        return this;
    }

    @Override
    public String getSql() {
        sqlBuilder.setLength(0);
        sqlBuilder.append("DELETE FROM ").append(table);

        if (whereCondition != null) {
            sqlBuilder.append(" WHERE ").append(whereCondition.getSql());
            parameters.addAll(whereCondition.getParameters());
        }

        return sqlBuilder.toString();
    }
}
