package io.github.linsminecraftstudio.mxlib.database.impl;

import com.google.common.base.Strings;
import io.github.linsminecraftstudio.mxlib.database.DatabaseConnection;
import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.AutoIncrement;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.Column;
import io.github.linsminecraftstudio.mxlib.database.serialization.ObjectSerializer;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.PrimaryKey;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.Table;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class AbstractSQLConnection implements DatabaseConnection {

    abstract Connection getConnection() throws SQLException;

    @Override
    public abstract @NotNull DatabaseType getType();

    @Override
    public abstract void close();

    @Override
    public boolean execute(SQL sql) throws SQLException {
        try (Connection connection = getConnection()) {
            return sql.build(connection).execute();
        }
    }

    @Override
    public ResultSet query(SelectSQL sql) throws SQLException {
        try (Connection connection = getConnection()) {
            return sql.build(connection).executeQuery();
        }
    }

    @Override
    public <T> @NotNull T selectOne(Class<T> clazz, @NotNull Condition condition) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        SelectSQL sql = SQL.select()
                .allColumns()
                .from(table.name())
                .where(condition)
                .limit(1);

        ResultSet query = query(sql);
        return ObjectSerializer.serializeOne(clazz, query);
    }

    @Override
    public <T> @NotNull List<T> selectMulti(@NotNull Class<T> clazz) throws SQLException {
        return selectMulti(clazz, null);
    }

    @Override
    public <T> @NotNull List<T> selectMulti(@NotNull Class<T> clazz, @Nullable Condition condition) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        SelectSQL sql = SQL.select()
                .allColumns()
                .from(table.name());

        if (condition != null) {
            sql.where(condition);
        }

        ResultSet set = query(sql);
        return ObjectSerializer.serializeMulti(clazz, set);
    }

    @Override
    public boolean ping() throws SQLException {
        try (Connection connection = getConnection()) {
            return connection.isValid(1);
        }
    }

    @Override
    public void rollback() throws SQLException {
        try (Connection connection = getConnection()) {
            connection.rollback();
        }
    }

    @Override
    public void commit() throws SQLException {
        try (Connection connection = getConnection()) {
            connection.commit();
        }
    }

    @Override
    public void createTableByClass(@NotNull Class<?> clazz) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        List<Field> field = ObjectSerializer.getAllFields(clazz);

        CreateTableSQL sql = SQL.createTable().table(table.name()).ifNotExists();

        for (Field f : field) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                if (column != null) {
                    Class<?> type = f.getType();

                    String sqlType = ObjectSerializer.getSqlType(type);
                    String columnName = ObjectSerializer.getColumnName(f);

                    sql.column(columnName, sqlType);

                    if (f.isAnnotationPresent(AutoIncrement.class)) {
                        sql.autoIncrement(columnName);
                    }

                    if (f.isAnnotationPresent(PrimaryKey.class)) {
                        sql.primaryKey(columnName);
                    }

                    if (!column.nullable()) {
                        sql.notNull(columnName);
                    }

                    if (!Strings.isNullOrEmpty(column.defaultValue())) {
                        sql.defaultValue(columnName, column.defaultValue());
                    }
                }
            }
        }

        sql.build(getConnection()).execute();
    }

    @Override
    public <T> void upsertObject(@NotNull Class<T> clazz, @NotNull T object) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        List<Field> field = ObjectSerializer.getAllFields(clazz);

        InsertSQL sql = SQL.upsert().into(table.name());

        for (Field f : field) {
            if (f.isAnnotationPresent(Column.class)) {
                String columnName = ObjectSerializer.getColumnName(f);
                try {
                    sql.value(columnName, f.get(object));
                } catch (IllegalAccessException e) {
                    //never happen
                    throw new RuntimeException(e);
                }
            }
        }

        sql.build(getConnection()).execute();
    }

    @Override
    public <T> void updateObject(@NotNull Class<T> clazz, @NotNull T object, @NotNull Condition condition) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        List<Field> field = ObjectSerializer.getAllFields(clazz);

        UpdateSQL sql = SQL.update().table(table.name());
        for (Field f : field) {
            if (f.isAnnotationPresent(Column.class)) {
                String columnName = ObjectSerializer.getColumnName(f);
                try {
                    sql.set(columnName, f.get(object));
                } catch (IllegalAccessException e) {
                    //never happen
                    throw new RuntimeException(e);
                }
            }
        }

        sql.where(condition);

        sql.build(getConnection()).execute();
    }

    @Override
    public void deleteObject(@NotNull Class<?> clazz, @NotNull Condition condition) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        DeleteSQL sql = SQL.delete().from(table.name()).where(condition);
        sql.build(getConnection()).execute();
    }
}