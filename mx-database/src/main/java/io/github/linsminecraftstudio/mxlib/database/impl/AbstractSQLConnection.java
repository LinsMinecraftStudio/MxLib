package io.github.linsminecraftstudio.mxlib.database.impl;

import com.google.common.base.Strings;
import io.github.linsminecraftstudio.mxlib.database.DatabaseConnection;
import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.AutoIncrement;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.Column;
import io.github.linsminecraftstudio.mxlib.database.serialization.ObjectSerializer;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.PrimaryKey;
import io.github.linsminecraftstudio.mxlib.database.serialization.annotations.Table;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.CreateTableSQL;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SQL;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SelectSQL;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class AbstractSQLConnection implements DatabaseConnection {

    abstract Connection getConnection() throws SQLException;

    @Override
    public abstract DatabaseType getType();

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
    public <T> T selectOne(Class<T> clazz) throws SQLException {
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
                .limit(1);
        ResultSet query = query(sql);
        return ObjectSerializer.serializeOne(clazz, query);
    }

    @Override
    public <T> List<T> selectMulti(Class<T> clazz) throws SQLException {
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
    public void createTableByClass(Class<?> clazz) throws SQLException {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("the class must be annotated with @Table");
        }

        Table table = clazz.getAnnotation(Table.class);
        if (Strings.isNullOrEmpty(table.name())) {
            throw new IllegalArgumentException("the table name cannot be empty");
        }

        List<Field> field = ObjectSerializer.getAllFields(clazz);

        CreateTableSQL sql = SQL.createTable().table(table.name());

        for (Field f : field) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                if (column != null) {
                    Class<?> type = f.getType();

                    String sqlType = ObjectSerializer.getSqlType(type);
                    sql.column(column.name(), sqlType);

                    if (f.isAnnotationPresent(AutoIncrement.class)) {
                        sql.autoIncrement(column.name());
                    }

                    if (f.isAnnotationPresent(PrimaryKey.class)) {
                        sql.primaryKey(column.name());
                    }

                    if (!column.nullable()) {
                        sql.notNull(column.name());
                    }

                    if (!Strings.isNullOrEmpty(column.defaultValue())) {
                        sql.defaultValue(column.name(), column.defaultValue());
                    }
                }
            }
        }

        sql.build(getConnection()).execute();
    }
}