package org.see.mao.persistence;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a single-valued association to another entity that has
 * one-to-one multiplicity.
 * 暂时不支持通过中间表进行一对一操作
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface OneToOne {

    /**
     * The field that owns the relationship. Required unless
     * the relationship is unidirectional.
     */
    String mappedBy();
    
    /**
     * (Optional) The entity class that is the target of
     * the association.
     *
     * <p> Defaults to the type of the field or property
     * that stores the association.
     */
	Class<?> targetEntity();
	
	
	/**
     * 关联数据库表中字段名称
     * 		如果不设置，那么默认关联另一数据库表的id
	 */
	String inverseColumnName() default "";
	
}
