package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CreateTableSQL extends SQL {
    private final Map<String, ColumnDefinition> columns = new LinkedHashMap<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();
    private final List<String> uniqueConstraints = new ArrayList<>();
    private final List<String> checks = new ArrayList<>();
    private final List<String> primaryKeys = new ArrayList<>();

    private String tableName;
    private boolean ifNotExists;
    private String tableOptions;

    CreateTableSQL() {}

    public CreateTableSQL table(String tableName) {
        validateIdentifier(tableName);
        this.tableName = tableName;
        return this;
    }

    public CreateTableSQL ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    public CreateTableSQL column(String name, String dataType) {
        return column(name, dataType, null);
    }

    public CreateTableSQL column(String name, String dataType, Integer length) {
        validateIdentifier(name);
        columns.put(name, new ColumnDefinition(dataType, length, false, false, null));
        return this;
    }

    public CreateTableSQL notNull(String columnName) {
        getColumn(columnName).nullable = false;
        return this;
    }

    public CreateTableSQL nullable(String columnName) {
        getColumn(columnName).nullable = true;
        return this;
    }

    public CreateTableSQL defaultValue(String columnName, String defaultValue) {
        getColumn(columnName).defaultValue = defaultValue;
        return this;
    }

    public CreateTableSQL autoIncrement(String columnName) {
        getColumn(columnName).autoIncrement = true;
        return this;
    }

    public CreateTableSQL primaryKey(String... columnNames) {
        for (String columnName : columnNames) {
            validateIdentifier(columnName);
            if (!columns.containsKey(columnName)) {
                throw new IllegalArgumentException("Column not defined: " + columnName);
            }

            primaryKeys.add(columnName);
        }
        return this;
    }


    public CreateTableSQL foreignKey(String column, String referenceTable, String referenceColumn) {
        validateIdentifier(column);
        validateIdentifier(referenceTable);
        validateIdentifier(referenceColumn);
        foreignKeys.add(new ForeignKey(column, referenceTable, referenceColumn));
        return this;
    }

    public CreateTableSQL unique(String... columnNames) {
        for (String columnName : columnNames) {
            validateIdentifier(columnName);
            uniqueConstraints.add(columnName);
        }
        return this;
    }

    public CreateTableSQL check(String condition) {
        checks.add(condition);
        return this;
    }

    public CreateTableSQL options(String options) {
        this.tableOptions = options;
        return this;
    }

    private String adaptTableOptions(String options, DatabaseType type) {
        if (type == DatabaseType.SQLITE) {
            return options.replaceAll("ENGINE=\\w+", "")
                    .replaceAll("DEFAULT CHARSET=\\w+", "")
                    .trim();
        }
        return options;
    }

    @Override
    public PreparedStatement build(Connection connection, DatabaseType type) throws SQLException {
        return connection.prepareStatement(getSql(type));
    }

    private ColumnDefinition getColumn(String columnName) {
        ColumnDefinition column = columns.get(columnName);
        if (column == null) {
            throw new IllegalArgumentException("Column not defined: " + columnName);
        }
        return column;
    }

    protected void validateIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }

    @Override
    public String getSql(DatabaseType type) {
        if (tableName == null) {
            throw new IllegalStateException("Table name must be specified");
        }

        if (columns.isEmpty()) {
            throw new IllegalStateException("At least one column must be defined");
        }

        StringBuilder sql = new StringBuilder("CREATE TABLE");

        if (ifNotExists) {
            sql.append(" IF NOT EXISTS");
        }

        sql.append(" ").append(tableName).append(" (\n");

        List<String> columnDefs = columns.entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    ColumnDefinition def = entry.getValue();
                    return buildColumnDefinition(name, def, type);
                })
                .collect(Collectors.toList());

        if (!primaryKeys.isEmpty()) {
            columnDefs.add("  PRIMARY KEY (" + String.join(", ", primaryKeys) + ")");
        }

        if (!uniqueConstraints.isEmpty()) {
            columnDefs.add("  UNIQUE (" + String.join(", ", uniqueConstraints) + ")");
        }

        for (ForeignKey fk : foreignKeys) {
            columnDefs.add(String.format(
                    "  FOREIGN KEY (%s) REFERENCES %s(%s)",
                    fk.column, fk.referenceTable, fk.referenceColumn
            ));
        }

        for (String check : checks) {
            columnDefs.add("  CHECK (" + check + ")");
        }

        sql.append(String.join(",\n", columnDefs));
        sql.append("\n)");

        if (tableOptions != null && !tableOptions.isEmpty()) {
            sql.append(" ").append(adaptTableOptions(tableOptions, type));
        }

        return sql.toString();
    }

    private String buildColumnDefinition(String name, ColumnDefinition def, DatabaseType type) {
        StringBuilder colDef = new StringBuilder("  ").append(name).append(" ").append(def.dataType);

        if (def.autoIncrement) {
            if (type == DatabaseType.MYSQL) {
                colDef.append(" AUTO_INCREMENT");
            } else if (type == DatabaseType.SQLITE) {
                colDef.append(" AUTOINCREMENT");
            }
        }

        if (!primaryKeys.contains(name)) {
            if (!def.nullable) {
                colDef.append(" NOT NULL");
            }
        }

        if (def.defaultValue != null) {
            colDef.append(" DEFAULT ").append(def.defaultValue);
        }

        return colDef.toString();
    }

    private static class ColumnDefinition {
        final String dataType;
        final Integer length;
        boolean nullable;
        boolean autoIncrement;
        String defaultValue;

        ColumnDefinition(String dataType, Integer length, boolean nullable,
                         boolean autoIncrement, String defaultValue) {
            this.dataType = dataType;
            this.length = length;
            this.nullable = nullable;
            this.autoIncrement = autoIncrement;
            this.defaultValue = defaultValue;
        }
    }

    private record ForeignKey(String column, String referenceTable, String referenceColumn) {
    }
}