package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CreateTableBuilder extends AbstractSqlBuilder {
    private String tableName;
    private final Map<String, ColumnDefinition> columns = new LinkedHashMap<>();
    private final List<String> primaryKeys = new ArrayList<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();
    private final List<String> uniqueConstraints = new ArrayList<>();
    private final List<String> checks = new ArrayList<>();
    private String tableOptions;

    public CreateTableBuilder table(String tableName) {
        validateIdentifier(tableName);
        this.tableName = tableName;
        return this;
    }

    public CreateTableBuilder column(String name, String dataType) {
        return column(name, dataType, null);
    }

    public CreateTableBuilder column(String name, String dataType, Integer length) {
        validateIdentifier(name);
        columns.put(name, new ColumnDefinition(dataType, length, false, true, null, null));
        return this;
    }

    public CreateTableBuilder notNull(String columnName) {
        getColumn(columnName).nullable = false;
        return this;
    }

    public CreateTableBuilder nullable(String columnName) {
        getColumn(columnName).nullable = true;
        return this;
    }

    public CreateTableBuilder defaultValue(String columnName, String defaultValue) {
        getColumn(columnName).defaultValue = defaultValue;
        return this;
    }

    public CreateTableBuilder autoIncrement(String columnName) {
        getColumn(columnName).autoIncrement = true;
        return this;
    }

    public CreateTableBuilder primaryKey(String... columnNames) {
        for (String columnName : columnNames) {
            validateIdentifier(columnName);
            primaryKeys.add(columnName);
        }
        return this;
    }

    public CreateTableBuilder foreignKey(String column, String referenceTable, String referenceColumn) {
        validateIdentifier(column);
        validateIdentifier(referenceTable);
        validateIdentifier(referenceColumn);
        foreignKeys.add(new ForeignKey(column, referenceTable, referenceColumn));
        return this;
    }

    public CreateTableBuilder unique(String... columnNames) {
        for (String columnName : columnNames) {
            validateIdentifier(columnName);
            uniqueConstraints.add(columnName);
        }
        return this;
    }

    public CreateTableBuilder check(String condition) {
        checks.add(condition);
        return this;
    }

    public CreateTableBuilder options(String options) {
        this.tableOptions = options;
        return this;
    }

    @Override
    public String getSql() {
        if (tableName == null) {
            throw new IllegalStateException("Table name must be specified");
        }
        if (columns.isEmpty()) {
            throw new IllegalStateException("At least one column must be defined");
        }

        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(tableName).append(" (\n");

        // Column definitions
        List<String> columnDefs = columns.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    ColumnDefinition def = entry.getValue();
                    return buildColumnDefinition(name, def);
                })
                .collect(Collectors.toList());

        // Primary key
        if (!primaryKeys.isEmpty()) {
            columnDefs.add("  PRIMARY KEY (" + String.join(", ", primaryKeys) + ")");
        }

        // Unique constraints
        if (!uniqueConstraints.isEmpty()) {
            columnDefs.add("  UNIQUE (" + String.join(", ", uniqueConstraints) + ")");
        }

        // Foreign keys
        for (ForeignKey fk : foreignKeys) {
            columnDefs.add(String.format(
                    "  FOREIGN KEY (%s) REFERENCES %s(%s)",
                    fk.column, fk.referenceTable, fk.referenceColumn
            ));
        }

        // Check constraints
        for (String check : checks) {
            columnDefs.add("  CHECK (" + check + ")");
        }

        sql.append(String.join(",\n", columnDefs));
        sql.append("\n)");

        if (tableOptions != null && !tableOptions.isEmpty()) {
            sql.append(" ").append(tableOptions);
        }

        return sql.toString();
    }

    @Override
    public List<Object> getParameters() {
        return Collections.emptyList(); // CREATE TABLE typically has no parameters
    }

    @Override
    public PreparedStatement build(Connection connection) throws SQLException {
        return connection.prepareStatement(getSql());
    }

    private ColumnDefinition getColumn(String columnName) {
        ColumnDefinition column = columns.get(columnName);
        if (column == null) {
            throw new IllegalArgumentException("Column not defined: " + columnName);
        }
        return column;
    }

    private String buildColumnDefinition(String name, ColumnDefinition def) {
        StringBuilder sb = new StringBuilder("  ");
        sb.append(name).append(" ").append(def.dataType);

        if (def.length != null) {
            sb.append("(").append(def.length).append(")");
        }

        if (!def.nullable) {
            sb.append(" NOT NULL");
        }

        if (def.defaultValue != null) {
            sb.append(" DEFAULT ").append(def.defaultValue);
        }

        if (def.autoIncrement) {
            sb.append(" AUTO_INCREMENT");
        }

        return sb.toString();
    }

    protected void validateIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }

    private static class ColumnDefinition {
        final String dataType;
        final Integer length;
        boolean nullable;
        boolean autoIncrement;
        String defaultValue;
        String comment;

        ColumnDefinition(String dataType, Integer length, boolean nullable,
                         boolean autoIncrement, String defaultValue, String comment) {
            this.dataType = dataType;
            this.length = length;
            this.nullable = nullable;
            this.autoIncrement = autoIncrement;
            this.defaultValue = defaultValue;
            this.comment = comment;
        }
    }

    private static class ForeignKey {
        final String column;
        final String referenceTable;
        final String referenceColumn;

        ForeignKey(String column, String referenceTable, String referenceColumn) {
            this.column = column;
            this.referenceTable = referenceTable;
            this.referenceColumn = referenceColumn;
        }
    }
}