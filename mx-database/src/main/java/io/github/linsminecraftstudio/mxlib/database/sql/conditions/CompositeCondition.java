package io.github.linsminecraftstudio.mxlib.database.sql.conditions;

import java.util.ArrayList;
import java.util.List;

public class CompositeCondition implements Condition {
    private final Condition left;
    private final Condition right;
    private final String operator;

    public CompositeCondition(Condition left, Condition right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public String getSql() {
        return "(" + left.getSql() + " " + operator + " " + right.getSql() + ")";
    }

    @Override
    public List<Object> getParameters() {
        List<Object> params = new ArrayList<>();
        params.addAll(left.getParameters());
        params.addAll(right.getParameters());
        return params;
    }
}
