package org.see.mao.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.see.mao.persistence.FetchType.LAZY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies a column for joining an entity association or element collection.
 * If the <code>JoinColumn</code> annotation itself is defaulted, a single join
 * column is assumed and the default values apply.
 */
@Target(FIELD)
@Retention(RUNTIME)
@SuppressWarnings("rawtypes")
public @interface JoinColumn {

	/**
	 * (Optional) The name of the foreign key column. The table in which it is
	 * found depends upon the context.
	 */
	String name();

	/**
	 * (Optional) The entity class that is the target of the association.
	 *
	 * <p>
	 * Defaults to the type of the field or property that stores the
	 * association.
	 */
	Class targetEntity();
	
    /** (Optional) Whether the association should be lazily loaded or
     * must be eagerly fetched. The EAGER strategy is a requirement on
     * the persistence provider runtime that the associated entities
     * must be eagerly fetched.  The LAZY strategy is a hint to the
     * persistence provider runtime.
     */
    FetchType fetch() default LAZY;
    
}
