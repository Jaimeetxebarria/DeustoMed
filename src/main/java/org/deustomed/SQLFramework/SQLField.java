package org.deustomed.SQLFramework;

import java.lang.annotation.*;


@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLField {
    String name();

    Class<?> type();

    boolean isPrimary() default false;
}
