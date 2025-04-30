package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

public class DropSQL extends SQL {
    private String name;
    private boolean ifExists;
    private boolean isTable;
    private boolean isIndex;
    private String tableName; // 仅用于 DROP INDEX

    DropSQL() {}

    private DropSQL name(String name) {
        validateIdentifier(name);
        this.name = name;
        return this;
    }

    public DropSQL ifExists() {
        this.ifExists = true;
        return this;
    }

    public DropSQL table(String tableName) {
        this.isTable = true;
        this.isIndex = false;
        return name(tableName);
    }

    public DropSQL index(String indexName) {
        this.isTable = false;
        this.isIndex = true;
        return name(indexName);
    }

    public DropSQL fromTable(String tableName) {
        validateIdentifier(tableName);
        this.tableName = tableName;
        return this;
    }

    @Override
    public String getSql() {
        sqlBuilder.setLength(0);

        if (isTable) {
            buildDropTable();
        } else if (isIndex) {
            buildDropIndex();
        } else {
            throw new IllegalStateException("Neither table nor index specified");
        }

        return sqlBuilder.toString();
    }

    private void buildDropTable() {
        sqlBuilder.append("DROP TABLE ");
        appendIfExists();
        sqlBuilder.append(name);
    }

    private void buildDropIndex() {
        sqlBuilder.append("DROP INDEX ");
        appendIfExists();
        sqlBuilder.append(name);

        if (tableName != null) {
            sqlBuilder.append(" ON ").append(tableName);
        }
    }

    private void appendIfExists() {
        if (ifExists) {
            sqlBuilder.append("IF EXISTS ");
        }
    }
}