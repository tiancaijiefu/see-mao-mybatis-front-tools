package org.see.mao.helpers.sql.dbms;

import org.see.mao.helpers.AnnotationReflectionHelper;
import org.see.mao.persistence.MetaDataAnnotationConfig;

/**
 * @author Joshua Wang
 * @date 2016年12月2日
 */
public class SelectBuilder extends MaoSqlBuilder {
	
	/**
	 * 根据类获取sql
	 * 		根据id获取唯一数据
	 * @return
	 */
	public static String getUseIdSelectSql(Class<?> clazz){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflectionHelper.getAnnotationConfig(clazz);
		String table  = annotationConfig.getTableName();
		String table_ = table+"_";
		String columns = getColumnsStr(clazz, table_);
		StringBuilder sqlBuilder = new StringBuilder("select ").append(table_+".id").append(",");
		sqlBuilder.append(columns);
		sqlBuilder.append(" from ").append(table).append(" ").append(table_);
		sqlBuilder.append(" where ").append(table_+".id=#{id}");
		return sqlBuilder.toString();
	}
	
	/**
	 * 根据指定字段名称获取条件查询sql
	 * @param clazz
	 * @param whereColumn
	 * @return
	 */
	public static String getUserWhereColumnSelectSql(Class<?> clazz,String whereColumn){
		MetaDataAnnotationConfig annotationConfig = AnnotationReflectionHelper.getAnnotationConfig(clazz);
		String table  = annotationConfig.getTableName();
		String table_ = table+"_";
		String columns = getColumnsStr(clazz, table_);
		StringBuilder sqlBuilder = new StringBuilder("select ").append(table_+".id").append(",");
		sqlBuilder.append(columns);
		sqlBuilder.append(" from ").append(table).append(" ").append(table_);
		sqlBuilder.append(" where ").append(table_+"."+whereColumn+"=#{id}");
		return sqlBuilder.toString();
	}
	
}
