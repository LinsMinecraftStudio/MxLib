package io.github.linsminecraftstudio.mxlib.database.sql.conditions;

import java.util.List;

public interface Condition {
    String getSql();
    List<Object> getParameters();
}

