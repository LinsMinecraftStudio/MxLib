package io.github.linsminecraftstudio.mxlib.database.enums;

public enum DatabaseType {
    SQLITE,
    MYSQL,
    MARIADB;

    public static DatabaseType getByName(String name) {
        for (DatabaseType value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
