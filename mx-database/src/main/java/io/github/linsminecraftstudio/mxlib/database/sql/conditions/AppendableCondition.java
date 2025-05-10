package io.github.linsminecraftstudio.mxlib.database.sql.conditions;

import java.util.ArrayList;
import java.util.List;

class AppendableCondition implements Condition {
    private final Condition[] multi;
    private final String operator;

    public AppendableCondition(Condition[] multi, String operator) {
        this.multi = multi;
        this.operator = operator;
    }

    @Override
    public String getSql() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("(");
        for (Condition condition : multi) {
            sqlBuilder.append(condition.getSql());
            if (condition != multi[multi.length - 1]) {
                sqlBuilder.append(" ").append(operator).append(" ");
            }
        }

        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    @Override
    public List<Object> getParameters() {
        List<Object> params = new ArrayList<>();
        for (Condition condition : multi) {
            params.addAll(condition.getParameters());
        }
        System.out.println("params count:" + params.size());
        return params;
    }
}
