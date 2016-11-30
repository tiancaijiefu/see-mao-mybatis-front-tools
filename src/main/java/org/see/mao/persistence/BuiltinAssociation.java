package org.see.mao.persistence;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 类内置关联属性标识
 * 		如果类中存在关联属性，则必须声明此Annotation
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface BuiltinAssociation {

}
