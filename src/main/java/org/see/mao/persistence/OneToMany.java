package org.see.mao.persistence;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a single-valued association to another entity that has
 * one-to-one multiplicity.
 */
@Target(ElementType.FIELD)
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
	
	/**
	 * 是否需要中间表
	 * @return
	 */
	boolean interTable() default false;
	
	/**
	 * 中间表名称
	 * @return
	 */
	String interTableName() default "";
	
	/**
	 * 当前类对应数据库表在中间表中字段名称
	 */
	String refColumnName() default "";
	
	/**
     * 关联类对应数据库表在中间表中字段名称
	 */
	String inverseColumnName() default "";
	
	/**
	 * 如果不需要中间表，那么关联指定类对应数据库表中字段名称
	 * @return
	 */
	String associateColumnName() default "";

}
