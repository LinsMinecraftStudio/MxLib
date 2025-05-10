package io.github.linsminecraftstudio.mxlib.database.serialization.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String AUTO_NAMED = "AUTO_FOLLOW_FIELD_NAME";

    String name() default AUTO_NAMED;

    String defaultValue() default "";

    boolean nullable() default true;
}
