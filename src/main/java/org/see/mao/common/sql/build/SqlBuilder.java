package org.see.mao.common.sql.build;

import java.lang.reflect.Field;
import java.util.List;

import org.see.mao.common.reflex.AnnotationReflections;
import org.see.mao.persistence.AnnotationTag;
import org.see.mao.persistence.MetaDataAnnotationConfig;
import org.see.mao.persistence.VersionConfig;

import com.google.common.collect.Lists;

/**
 * @author Joshua Wang
 * @date 2016年12月1日
 */
public abstract class SqlBuilder {
	
	private static final List<String> versionColumns = Lists.newArrayList();
	
	static{
		versionColumns.add(VersionConfig.CREATE_USERE+" "+"createUser");
		versionColumns.add(VersionConfig.UPDATE_USER+" "+"updateUser");
		versionColumns.add(VersionConfig.CREATE_TIME+" "+"createTime");
		versionColumns.add(VersionConfig.UPDATE_TIME+" "+"updateTime");
	}
	
	/**
	 * 根据Class获取注解定义的所有字段[原始字段，不带别名Alias]
	 * @param clazz
	 * @return
	 */
	public static List<String> getColumns(Class<?> clazz){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
		List<Field> fieldList = annotationConfig.getColumnFields();
		List<String> columns = Lists.newArrayList();
		int len = fieldList.size();
		for (int i = 0; i < len; i++) {
			Field field = fieldList.get(i);
			columns.add(AnnotationTag.getColumn(field)+" "+field.getName());
		}
		//version
		if(annotationConfig.isVersion()){
			columns.addAll(versionColumns);
		}
		return columns;
	}
	
	/**
	 * 根据Class获取注解定义的所有字段[原始字段，带别名Alias]
	 * @param clazz
	 * @return
	 */
	public static List<String> getColumns(Class<?> clazz,String alias){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
		List<Field> fieldList = annotationConfig.getColumnFields();
		List<String> columns = Lists.newArrayList();
		int len = fieldList.size();
		for (int i = 0; i < len; i++) {
			Field field = fieldList.get(i);
			columns.add(AnnotationTag.getColumn(fieldList.get(i), alias)+" "+field.getName());
		}
		//version
		if(annotationConfig.isVersion()){
			for(String column : versionColumns){
				columns.add(alias+"."+column);
			}
		}
		return columns;
	}
	
	/**
	 * 根据Class获取注解定义的所有字段的字符串拼接[原始字段，不带别名Alias]
	 * @param clazz
	 * @return
	 */
	public static String getColumnsStr(Class<?> clazz){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
		List<Field> fieldList = annotationConfig.getColumnFields();
		StringBuilder builder = new StringBuilder();
		int len = fieldList.size();
		for (int i = 0; i < len; i++) {
			Field field = fieldList.get(i);
			if(i != 0){
				builder.append(",");
			}
			builder.append(AnnotationTag.getColumn(fieldList.get(i))+" "+field.getName());
		}
		//version
		if(annotationConfig.isVersion()){
			for(String column : versionColumns){
				builder.append(","+column);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 根据Class获取注解定义的所有字段的字符串拼接[原始字段，带别名Alias]
	 * @param clazz
	 * @return
	 */
	public static String getColumnsStr(Class<?> clazz,String alias){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
		List<Field> fieldList = annotationConfig.getColumnFields();
		StringBuilder builder = new StringBuilder();
		int len = fieldList.size();
		for (int i = 0; i < len; i++) {
			Field field = fieldList.get(i);
			if(i != 0){
				builder.append(",");
			}
			builder.append(AnnotationTag.getColumn(fieldList.get(i),alias)+" "+field.getName());
		}
		//version   alias+"."+column
		if(annotationConfig.isVersion()){
			for(String column : versionColumns){
				builder.append(","+alias+"."+column);
			}
		}
		return builder.toString();
	}
}
