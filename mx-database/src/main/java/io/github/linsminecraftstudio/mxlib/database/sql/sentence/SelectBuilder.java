package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.enums.JoinType;
import io.github.linsminecraftstudio.mxlib.database.sql.AbstractSqlBuilder;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectBuilder extends AbstractSqlBuilder {
    private boolean distinct = false;
    private final List<String> columns = new ArrayList<>();
    private String table;
    private final List<JoinClause> joins = new ArrayList<>();
    private Condition whereCondition;
    private final List<GroupBy> groupByList = new ArrayList<>();
    private Condition havingCondition;
    private final List<OrderBy> orderByList = new ArrayList<>();
    private Integer limit;
    private Integer offset;
    private boolean forUpdate = false;
    private final List<Object> parameters = new ArrayList<>();

    public SelectBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public SelectBuilder from(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public SelectBuilder column(String column) {
        validateIdentifier(column);
        this.columns.add(column);
        return this;
    }

    public SelectBuilder column(String column, String alias) {
        validateIdentifier(column);
        if (alias != null) validateIdentifier(alias);
        this.columns.add(column + (alias != null ? " AS " + alias : ""));
        return this;
    }

    public SelectBuilder columns(String... columns) {
        for (String column : columns) {
            column(column);
        }
        return this;
    }

    public SelectBuilder allColumns() {
        this.columns.add("*");
        return this;
    }

    public SelectBuilder join(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.INNER);
    }

    public SelectBuilder join(String table, String onClause, JoinType joinType) {
        validateIdentifier(table);
        this.joins.add(new JoinClause(table, onClause, joinType));
        return this;
    }

    public SelectBuilder leftJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.LEFT);
    }

    public SelectBuilder rightJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.RIGHT);
    }

    public SelectBuilder fullJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.FULL);
    }

    public SelectBuilder where(Condition condition) {
        this.whereCondition = condition;
        return this;
    }

    public SelectBuilder groupBy(String column) {
        validateIdentifier(column);
        this.groupByList.add(new GroupBy(column));
        return this;
    }

    public SelectBuilder groupBy(String column, String... additionalColumns) {
        groupBy(column);
        for (String col : additionalColumns) {
            groupBy(col);
        }
        return this;
    }

    public SelectBuilder having(Condition condition) {
        this.havingCondition = condition;
        return this;
    }

    public SelectBuilder orderBy(String column) {
        return orderBy(column, OrderType.ASC);
    }

    public SelectBuilder orderBy(String column, OrderType orderType) {
        validateIdentifier(column);
        this.orderByList.add(new OrderBy(column, orderType));
        return this;
    }

    public SelectBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectBuilder offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectBuilder forUpdate() {
        this.forUpdate = true;
        return this;
    }

    @Override
    public String getSql() {
        if (table == null) {
            throw new IllegalStateException("Table must be specified");
        }

        if (columns.isEmpty()) {
            throw new IllegalStateException("At least one column must be selected");
        }

        StringBuilder sql = new StringBuilder("SELECT ");

        if (distinct) {
            sql.append("DISTINCT ");
        }

        sql.append(String.join(", ", columns));
        sql.append(" FROM ").append(table);

        for (JoinClause join : joins) {
            sql.append(" ").append(join.joinType.getSql()).append(" JOIN ")
                    .append(join.table).append(" ON ").append(join.onClause);
        }

        if (whereCondition != null) {
            sql.append(" WHERE ").append(whereCondition.getSql());
            parameters.addAll(whereCondition.getParameters());
        }

        if (!groupByList.isEmpty()) {
            sql.append(" GROUP BY ")
                    .append(groupByList.stream()
                            .map(GroupBy::column)
                            .collect(Collectors.joining(", ")));
        }

        if (havingCondition != null) {
            sql.append(" HAVING ").append(havingCondition.getSql());
            parameters.addAll(havingCondition.getParameters());
        }

        if (!orderByList.isEmpty()) {
            sql.append(" ORDER BY ")
                    .append(orderByList.stream()
                            .map(ob -> ob.column + " " + ob.orderType)
                            .collect(Collectors.joining(", ")));
        }

        if (limit != null) {
            sql.append(" LIMIT ?");
            parameters.add(limit);
        }

        if (offset != null) {
            sql.append(" OFFSET ?");
            parameters.add(offset);
        }

        if (forUpdate) {
            sql.append(" FOR UPDATE");
        }

        return sql.toString();
    }

    @Override
    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public PreparedStatement build(Connection connection) throws SQLException {
        String sql = getSql();
        PreparedStatement stmt = connection.prepareStatement(sql);

        int paramIndex = 1;
        for (Object param : parameters) {
            stmt.setObject(paramIndex++, param);
        }

        return stmt;
    }

    protected void validateIdentifier(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }
        if (!identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*") && !identifier.equals("*")) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }

    private record JoinClause(String table, String onClause, JoinType joinType) { }
    private record GroupBy(String column) { }
    private record OrderBy(String column, OrderType orderType) { }

    public enum OrderType {
        ASC, DESC
    }
}