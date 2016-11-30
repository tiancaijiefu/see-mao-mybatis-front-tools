package org.see.mao.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies the primary key of an entity. The field or property to which the
 * <code>Id</code> annotation is applied should be one of the following types:
 * any Java primitive type; any primitive wrapper type;
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Id {

}
