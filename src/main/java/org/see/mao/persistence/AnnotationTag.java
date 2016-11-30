package org.see.mao.persistence;

import org.see.mao.exception.MaoException;

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
		Table table = clazz.getAnnotation(Table.class);
		if(null == table){
			throw new MaoException(clazz+"未设置@Table注解！");
		}
		return table;
	}
	
	/**
	 * 判断目标Class是否设置了org.see.mao.persistence.BuiltinAssociation
	 * @param clazz
	 * @return
	 */
	static boolean isSettingsBuiltinAssociation(Class<?> clazz){
		BuiltinAssociation association = clazz.getAnnotation(BuiltinAssociation.class);
		if(null == association){
			return true;
		}
		return false;
	}
	
}
