package io.github.linsminecraftstudio.mxlib.database;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SQL;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SelectSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseConnection {
    void close() throws SQLException;

    boolean execute(SQL sql) throws SQLException;

    ResultSet query(SelectSQL sql) throws SQLException;

    <T> T selectOne(Class<T> clazz) throws SQLException;

    <T> List<T> selectMulti(Class<T> clazz) throws SQLException;

    void createTableByClass(Class<?> clazz) throws SQLException;

    <T> void insertObject(Class<T> clazz, T object) throws SQLException;

    boolean ping() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    DatabaseType getType();
}
