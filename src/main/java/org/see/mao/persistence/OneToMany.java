package org.see.mao.persistence;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a single-valued association to another entity that has
 * one-to-one multiplicity.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface OneToMany {
	
    /**
     * (Optional) The entity class that is the target of
     * the association.
     *
     * <p> Defaults to the type of the field or property
     * that stores the association.
     */
    Class<?> targetEntity();
    

    /** (Optional) The field that owns the relationship. This
      * element is only specified on the inverse (non-owning)
      * side of the association.
     */
    String mappedBy() default "";
	
}
