package io.github.linsminecraftstudio.mxlib.database.impl;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.linsminecraftstudio.mxlib.database.DatabaseConnection;
import io.github.linsminecraftstudio.mxlib.database.DatabaseParameters;
import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SelectBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class SQLiteConnection implements DatabaseConnection {
    private static final String JDBC_URL_FORMAT = "jdbc:sqlite:%s";
    private static final String JDBC_DRIVER_CLASS_NAME = "org.sqlite.JDBC";

    private final HikariDataSource dataSource;

    public SQLiteConnection(String absolutePath, DatabaseParameters parameters) {
        Preconditions.checkArgument(absolutePath != null, "the absolute path of database file cannot be null");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(JDBC_URL_FORMAT, absolutePath));
        config.setDriverClassName(JDBC_DRIVER_CLASS_NAME);
        config.setIdleTimeout(parameters.getIdleTimeout());
        config.setMaximumPoolSize(parameters.getMaxPoolSize());
        dataSource = new HikariDataSource(config);
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public boolean execute(AbstractSqlBuilder sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return sql.build(connection).execute();
        }
    }

    @Override
    public ResultSet query(SelectBuilder sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return sql.build(connection).executeQuery();
        }
    }

    @Override
    public boolean ping() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1);
        }
    }

    @Override
    public void rollback() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.rollback();
        }
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }

    @Override
    public void commit() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.commit();
        }
    }
}
