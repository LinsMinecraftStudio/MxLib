package io.github.linsminecraftstudio.mxlib.database.sql.conditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Conditions {
    public static Condition eq(String column, Object value) {
        return new SimpleCondition(column, "=", value);
    }

    public static Condition ne(String column, Object value) {
        return new SimpleCondition(column, "<>", value);
    }

    public static Condition gt(String column, Object value) {
        return new SimpleCondition(column, ">", value);
    }

    public static Condition lt(String column, Object value) {
        return new SimpleCondition(column, "<", value);
    }

    public static Condition like(String column, String pattern) {
        return new SimpleCondition(column, "LIKE", pattern);
    }

    public static Condition isNull(String column) {
        return new SimpleCondition(column, "IS", null);
    }

    public static Condition isNotNull(String column) {
        return new SimpleCondition(column, "IS NOT", null);
    }

    public static Condition and(Condition left, Condition right) {
        return new CompositeCondition(left, right, "AND");
    }

    public static Condition or(Condition left, Condition right) {
        return new CompositeCondition(left, right, "OR");
    }

    public static Condition in(String column, List<?> values) {
        String placeholders = String.join(", ", Collections.nCopies(values.size(), "?"));
        return new SimpleCondition(column, "IN (" + placeholders + ")", values);
    }

    public static Condition notIn(String column, List<?> values) {
        String placeholders = String.join(", ", Collections.nCopies(values.size(), "?"));
        return new SimpleCondition(column, "NOT IN (" + placeholders + ")", values);
    }

    public static Condition between(String column, Object lower, Object upper) {
        return new SimpleCondition(column, "BETWEEN ? AND ?", Arrays.asList(lower, upper));
    }
}
