package io.github.linsminecraftstudio.mxlib.database.impl;

import io.github.linsminecraftstudio.mxlib.database.DatabaseConnection;
import io.github.linsminecraftstudio.mxlib.database.DatabaseParameters;

public final class SQLConnections {
    private SQLConnections() {
    }

    public static DatabaseConnection sqlite(String file, DatabaseParameters parameters) {
        return new SQLiteConnection(file, parameters);
    }

    public static DatabaseConnection mysql(String host, int port, String database, String username, String password, DatabaseParameters parameters) {
        return new MySQLConnection(host, port, database, username, password, parameters);
    }

    public static DatabaseConnection mariadb(String host, int port, String database, String username, String password, DatabaseParameters parameters) {
        return new MariaDBConnection(host, port, database, username, password, parameters);
    }
}
