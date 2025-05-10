package io.github.linsminecraftstudio.mxlib.database.impl;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.linsminecraftstudio.mxlib.database.DatabaseParameters;
import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

class SQLiteConnection extends AbstractSQLConnection {
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
    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public @NotNull DatabaseType getType() {
        return DatabaseType.SQLITE;
    }
}
