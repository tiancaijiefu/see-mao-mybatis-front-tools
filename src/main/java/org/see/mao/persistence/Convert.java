package org.see.mao.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The Convert annotation is used to specify the conversion of a Basic field or
 * property. It is not necessary to use the Basic annotation or corresponding
 * XML element to specify the basic type.
 */
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
@SuppressWarnings("rawtypes")
public @interface Convert {

	/**
	 * Specifies the converter to be applied. A value for this element must be
	 * specified if multiple converters would otherwise apply.
	 */
	Class converter() default void.class;

	/**
	 * The attributeName must be specified unless the Convert annotation is on
	 * an attribute of basic type or on an element collection of basic type. In
	 * these cases, attributeName must not be specified.
	 */
	String attributeName() default "";

	/**
	 * Used to disable an auto-apply or inherited converter. If
	 * disableConversion is true, the converter element should not be specified.
	 */
	boolean disableConversion() default false;
}
