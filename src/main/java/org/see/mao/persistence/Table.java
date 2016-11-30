package org.see.mao.persistence;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies the primary table for the annotated entity.
 * 
 * @author Joshua Wang
 * @date 2016年11月17日
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

	/**
	 * <p>
	 * 	(Optional) The name of the table.
	 * <p/>
	 */
	String name() default "";
	
	/**
	 * 是否加入版本控制信息
	 * 		creator's info and updater's info
	 * @return
	 */
	boolean version();
	
}
