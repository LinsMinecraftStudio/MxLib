package io.github.linsminecraftstudio.mxlib.database.impl;

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

class MariaDBConnection implements DatabaseConnection {
    private static final String JDBC_URL_FORMAT = "jdbc:mariadb://%s:%d/%s?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";

    private final HikariDataSource dataSource;

    public MariaDBConnection(String host, int port, String database, String username, String password, DatabaseParameters parameters) {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(JDBC_URL_FORMAT.formatted(host, port, database));
        cfg.setDriverClassName(JDBC_DRIVER_CLASS_NAME);
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setMaximumPoolSize(parameters.getMaxPoolSize());
        cfg.setAutoCommit(parameters.isAutoCommit());
        cfg.setIdleTimeout(parameters.getIdleTimeout());
        try (HikariDataSource dataSource = new HikariDataSource(cfg)) {
            this.dataSource = dataSource;
        }
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
        return DatabaseType.MARIADB;
    }

    @Override
    public void commit() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.commit();
        }
    }
}
