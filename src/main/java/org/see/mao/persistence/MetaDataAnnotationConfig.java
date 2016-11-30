package org.see.mao.persistence;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * MetaData's Annotation Config
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class MetaDataAnnotationConfig {
	
	/**
	 * MetaData中Column注解对应的所有属性集合
	 */
	private List<Field> columnFields = Lists.newArrayList();
	
	/**
	 * MetaData中JoinColumn注解对应的所有属性集合
	 */
	private List<Field> joinColumnFields = Lists.newArrayList();
	
	/**
	 * MetaData版本号对应的属性
	 */
	private boolean version;
	
	/**
	 * MetaData主键对应的属性
	 */
	private Field idField;
	
	/**
	 * MetaData对应的数据库表名
	 */
	private String tableName;

	/**
	 * @return the columnFields
	 */
	public List<Field> getColumnFields() {
		return columnFields;
	}

	/**
	 * @param columnFields the columnFields to set
	 */
	public void setColumnFields(Field field) {
		this.columnFields.add(field);
	}

	/**
	 * @return the joinColumnFields
	 */
	public List<Field> getJoinColumnFields() {
		return joinColumnFields;
	}

	/**
	 * @param joinColumnFields the joinColumnFields to set
	 */
	public void setJoinColumnFields(Field field) {
		this.joinColumnFields.add(field);
	}

	/**
	 * @return the version
	 */
	public boolean isVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}

	/**
	 * @return the idField
	 */
	public Field getIdField() {
		return idField;
	}

	/**
	 * @param idField the idField to set
	 */
	public void setIdField(Field idField) {
		this.idField = idField;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
