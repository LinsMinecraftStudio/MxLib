package io.github.linsminecraftstudio.mxlib.database;

import io.github.linsminecraftstudio.mxlib.database.enums.DatabaseType;
import io.github.linsminecraftstudio.mxlib.database.sql.conditions.Condition;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SQL;
import io.github.linsminecraftstudio.mxlib.database.sql.sentence.SelectSQL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseConnection {
    void close() throws SQLException;

    boolean execute(SQL sql) throws SQLException;

    ResultSet query(SelectSQL sql) throws SQLException;

    /**
     * Select one object
     * @param clazz the class of the object
     * @param condition the condition to select
     * @return the object
     * @param <T> the type
     */
    @NotNull <T> T selectOne(Class<T> clazz, @NotNull Condition condition) throws SQLException;

    /**
     * Select multiple objects
     * @param clazz the class of the object
     * @return a list of objects
     * @param <T> the type
     */
    @NotNull <T> List<T> selectMulti(@NotNull Class<T> clazz) throws SQLException;

    /**
     * Select multiple objects
     * @param clazz the class of the object
     * @param condition the condition to select
     * @return a list of objects
     * @param <T> the type
     */
    @NotNull <T> List<T> selectMulti(@NotNull Class<T> clazz, @Nullable Condition condition) throws SQLException;

    /**
     * Create a table by class
     * @param clazz the class of the object
     */
    void createTableByClass(@NotNull Class<?> clazz) throws SQLException;

    /**
     * Upsert object
     * @param clazz the class of the object
     * @param object the object to upsert
     * @param <T> the type
     */
    <T> void upsertObject(@NotNull Class<T> clazz, @NotNull T object) throws SQLException;

    /**
     * Update object
     * @param clazz the class of the object
     * @param object the object to update
     * @param condition the condition to update
     * @param <T> the type
     */
    <T> void updateObject(@NotNull Class<T> clazz, @NotNull T object, @NotNull Condition condition) throws SQLException;

    void deleteObject(@NotNull Class<?> clazz, @NotNull Condition condition) throws SQLException;

    boolean ping() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    /**
     * @see DatabaseType
     * @return The type of the database connection.
     */
    @NotNull DatabaseType getType();
}
