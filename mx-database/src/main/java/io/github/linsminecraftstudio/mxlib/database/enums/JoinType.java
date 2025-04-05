package io.github.linsminecraftstudio.mxlib.database.enums;

import lombok.Getter;

@Getter
public enum JoinType {
    INNER("INNER"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    FULL("FULL");

    final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }
}
