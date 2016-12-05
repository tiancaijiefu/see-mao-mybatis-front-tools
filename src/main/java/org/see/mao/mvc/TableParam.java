package org.see.mao.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 		Spring MVC argument resolver annotation.
 * </p>
 * @author mumu@yfyang
 * @author Joshua Wang
 * @since JDK 1.8
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableParam {

}
