package io.github.linsminecraftstudio.mxlib.database.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

interface SqlBuilder {
    String getSql();

    List<Object> getParameters();

    PreparedStatement build(Connection connection) throws SQLException;
}
