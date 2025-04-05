package io.github.linsminecraftstudio.mxlib.database.sql.sentence;

public final class SqlBuilders {
    private SqlBuilders() {}

    public static SelectBuilder select() {
        return new SelectBuilder();
    }

    public static InsertBuilder insert() {
        return new InsertBuilder();
    }

    public static UpdateBuilder update() {
        return new UpdateBuilder();
    }

    public static DeleteBuilder delete() {
        return new DeleteBuilder();
    }

    public static CreateTableBuilder createTable() {
        return new CreateTableBuilder();
    }
}