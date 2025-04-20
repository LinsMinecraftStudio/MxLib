package io.github.linsminecraftstudio.mxlib.database;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SelectBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseConnection {
    void close() throws SQLException;

    boolean execute(AbstractSqlBuilder sql) throws SQLException;

    ResultSet query(SelectBuilder sql) throws SQLException;

    <T> T selectOne(Class<T> clazz) throws SQLException;

    <T> List<T> selectMulti(Class<T> clazz) throws SQLException;

    boolean ping() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    DatabaseType getType();
}
