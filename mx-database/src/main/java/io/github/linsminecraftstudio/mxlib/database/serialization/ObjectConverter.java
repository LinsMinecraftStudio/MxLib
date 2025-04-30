package io.github.linsminecraftstudio.mxlib.database.serialization;

public interface ObjectConverter<T> {
    T convert(Object o);

    String getSqlType();
}
