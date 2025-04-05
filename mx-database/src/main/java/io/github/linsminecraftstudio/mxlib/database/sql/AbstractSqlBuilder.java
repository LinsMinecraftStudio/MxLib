package io.github.linsminecraftstudio.mxlib.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSqlBuilder implements SqlBuilder {
    protected final StringBuilder sqlBuilder = new StringBuilder();
    protected final List<Object> parameters = new ArrayList<>();

    @Override
    public String getSql() {
        return sqlBuilder.toString();
    }

    @Override
    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public PreparedStatement build(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(getSql());
        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }
        return stmt;
    }

    protected void addParameter(Object value) {
        parameters.add(value);
    }

    protected void validateIdentifier(String identifier) {
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }
}