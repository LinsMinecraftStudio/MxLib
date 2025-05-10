package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.enums.JoinType;
import io.github.linsminecraftstudio.mxlib.database.enums.OrderType;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectSQL extends SQL {
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

    SelectSQL() {}

    public SelectSQL distinct() {
        this.distinct = true;
        return this;
    }

    public SelectSQL from(String table) {
        validateIdentifier(table);
        this.table = table;
        return this;
    }

    public SelectSQL column(String column) {
        validateIdentifier(column);
        this.columns.add(column);
        return this;
    }

    public SelectSQL column(String column, String alias) {
        validateIdentifier(column);
        if (alias != null) validateIdentifier(alias);
        this.columns.add(column + (alias != null ? " AS " + alias : ""));
        return this;
    }

    public SelectSQL columns(String... columns) {
        for (String column : columns) {
            column(column);
        }
        return this;
    }

    public SelectSQL allColumns() {
        this.columns.add("*");
        return this;
    }

    public SelectSQL join(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.INNER);
    }

    public SelectSQL join(String table, String onClause, JoinType joinType) {
        validateIdentifier(table);
        this.joins.add(new JoinClause(table, onClause, joinType));
        return this;
    }

    public SelectSQL leftJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.LEFT);
    }

    public SelectSQL rightJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.RIGHT);
    }

    public SelectSQL fullJoin(String table, String onClause) {
        return join(table, onClause, io.github.linsminecraftstudio.mxlib.database.enums.JoinType.FULL);
    }

    public SelectSQL where(Condition condition) {
        this.whereCondition = condition;
        return this;
    }

    public SelectSQL groupBy(String column) {
        validateIdentifier(column);
        this.groupByList.add(new GroupBy(column));
        return this;
    }

    public SelectSQL groupBy(String column, String... additionalColumns) {
        groupBy(column);
        for (String col : additionalColumns) {
            groupBy(col);
        }
        return this;
    }

    public SelectSQL having(Condition condition) {
        this.havingCondition = condition;
        return this;
    }

    public SelectSQL orderBy(String column) {
        return orderBy(column, OrderType.ASC);
    }

    public SelectSQL orderBy(String column, OrderType orderType) {
        validateIdentifier(column);
        this.orderByList.add(new OrderBy(column, orderType));
        return this;
    }

    public SelectSQL limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectSQL offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectSQL forUpdate() {
        this.forUpdate = true;
        return this;
    }

    @Override
    public String getSql(DatabaseType type) {
        if (table == null) {
            throw new IllegalStateException("Table must be specified");
        }

        if (columns.isEmpty()) {
            throw new IllegalStateException("At least one column must be selected");
        }

        // 清空参数列表，确保每次调用getSql都是全新的
        parameters.clear();

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
    public PreparedStatement build(Connection connection, DatabaseType type) throws SQLException {
        String sql = getSql(type);
        PreparedStatement stmt = connection.prepareStatement(sql);

        for (int i = 0; i < parameters.size(); i++) {
            stmt.setObject(i + 1, parameters.get(i));
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
}