package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    String getSql() {
        return sqlBuilder.toString();
    }

    List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public PreparedStatement build(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(getSql());
        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
        }
        return stmt;
    }

    void validateIdentifier(String identifier) {
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }
}