package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SQL {
    protected final StringBuilder sqlBuilder = new StringBuilder();
    protected final List<Object> parameters = new ArrayList<>();

    public static SelectSQL select() {
        return new SelectSQL();
    }

    public static InsertSQL insert() {
        return new InsertSQL(false);
    }

    public static InsertSQL upsert() {
        return new InsertSQL(true);
    }

    public static UpdateSQL update() {
        return new UpdateSQL();
    }

    public static DeleteSQL delete() {
        return new DeleteSQL();
    }

    public static DropSQL drop() {
        return new DropSQL();
    }

    public static CreateTableSQL createTable() {
        return new CreateTableSQL();
    }

    abstract String getSql(DatabaseType type);

    public PreparedStatement build(Connection connection, DatabaseType type) throws SQLException {
        parameters.clear();
        String sql = getSql(type);
        PreparedStatement stmt = connection.prepareStatement(sql);

        int expectedParams = countParametersInSql(sql);
        if (parameters.size() != expectedParams) {
            throw new SQLException("Parameter count mismatch. Expected " + expectedParams +
                    " but got " + parameters.size());
        }

        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }

        return stmt;
    }

    private int countParametersInSql(String sql) {
        int count = 0;
        int index = -1;
        while ((index = sql.indexOf('?', index + 1)) != -1) {
            count++;
        }
        return count;
    }

    void validateIdentifier(String identifier) {
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }
}