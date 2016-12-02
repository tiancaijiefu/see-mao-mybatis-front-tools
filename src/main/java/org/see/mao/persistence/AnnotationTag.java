package org.see.mao.persistence;

import java.lang.reflect.Field;

import org.see.mao.exception.MaoException;
import org.see.mao.helpers.reflex.Reflections;

/**
 * @author Joshua Wang
 * @date 2016年11月30日
 */
public interface AnnotationTag {
	
	/**
	 * 获取Class标注的Table注解
	 * @param clazz
	 * @return
	 */
	static Table getTable(Class<?> clazz){
		clazz = Reflections.getUserClass(clazz);
		Table table = clazz.getAnnotation(Table.class);
		if(null == table){
			throw new MaoException(clazz+"未设置@Table注解！");
		}
		return table;
	}
	
	/**
	 * 根据Field获取@Column定义的字段名称[无别名]
	 * 		如果未设置Column's name，则返回Field名称
	 * @param field
	 * @return
	 */
	static String getColumn(Field field){
		Column column = field.getAnnotation(Column.class);
		String name = column.name();
		if("".equals(name)){
			return field.getName();
		}
		return name;
	}
	
	/**
	 * 根据Field获取@Column定义的字段名称[指定别名]
	 * 		如果未设置Column's name，则返回Field名称
	 * @param field
	 * @return
	 */
	static String getColumn(Field field,String alias){
		Column column = field.getAnnotation(Column.class);
		String name = column.name();
		if("".equals(name)){
			return alias+"."+field.getName();
		}
		return alias+"."+name;
	}
	
	/**
	 * 根据属性名称获取属性对应的get方法名称
	 * @param fieldName
	 * @return
	 */
	static String getMethodName(String fieldName){
		return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	}
	
}
