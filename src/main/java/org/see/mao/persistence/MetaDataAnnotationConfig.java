package org.see.mao.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * MetaData's Annotation Config
 * 
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class MetaDataAnnotationConfig {

	/**
	 * MetaData中Column注解对应的所有属性集合
	 */
	private List<Field> columnFields = Lists.newArrayList();

	/**
	 * MetaData中OneToOne注解对应的所有属性集合
	 */
	private List<Field> oneToOneFields = Lists.newArrayList();

	/**
	 * MetaData中OneToMany注解对应的所有属性集合
	 */
	private List<Field> oneToManyFields = Lists.newArrayList();
	
	/**
	 * MetaData版本号对应的属性
	 */
	private boolean version;

	/**
	 * MetaData关联对应的属性
	 */
	private boolean associate;

	/**
	 * MetaData主键对应的属性
	 */
	private Field idField;

	/**
	 * MetaData对应的数据库表名
	 */
	private String tableName;
	
	/**
	 * MetaData中有关联的属性对应的get方法名称
	 */
	private List<String> associateGetNames = Lists.newArrayList();
	
	/**
	 * MetaData中关联的属性的存储
	 * key -> methodName
	 * val -> Field
	 */
	private Map<String,Field> associateMap = Maps.newHashMap();
	
	/**
	 * @return the associateGetNames
	 */
	public List<String> getAssociateGetNames() {
		return associateGetNames;
	}
	
	/**
	 * @param associateGetNames the associateGetNames to set
	 */
	public void setAssociateGetNames(String methodName) {
		this.associateGetNames.add(methodName);
	}
	
	/**
	 * @return the associateMap
	 */
	public Map<String, Field> getAssociateMap() {
		return associateMap;
	}

	/**
	 * @param associateMap the associateMap to set
	 */
	public void setAssociateMap(String key,Field value) {
		this.associateMap.put(key, value);
	}
	
	/**
	 * 根据标注的JavaBean的get方法获取属性
	 * @param method
	 * @return
	 */
	public Field searchAssociateField(Method method){
		String key = method.getName();
		return associateMap.get(key);
	}

	/**
	 * @return the columnFields
	 */
	public List<Field> getColumnFields() {
		return columnFields;
	}

	/**
	 * @param columnFields
	 *            the columnFields to set
	 */
	public void setColumnFields(Field field) {
		this.columnFields.add(field);
	}

	/**
	 * @return the oneToOneFields
	 */
	public List<Field> getOneToOneFields() {
		return oneToOneFields;
	}

	/**
	 * @param oneToOneFields
	 *            the oneToOneFields to set
	 */
	public void setOneToOneFields(Field field) {
		this.oneToOneFields.add(field);
		settingAssociateInfo(field);
	}
	
	/**
	 * 设置类关联信息
	 * @param field
	 */
	private void settingAssociateInfo(Field field){
		String methodName = getMethodName(field);
		this.associateGetNames.add(methodName);
		this.associateMap.put(methodName, field);
	}

	/**
	 * @return the oneToManyFields
	 */
	public List<Field> getOneToManyFields() {
		return oneToManyFields;
	}

	/**
	 * @param oneToManyFields
	 *            the oneToManyFields to set
	 */
	public void setOneToManyFields(Field field) {
		this.oneToManyFields.add(field);
		settingAssociateInfo(field);
	}

	/**
	 * @return the version
	 */
	public boolean isVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}

	/**
	 * @return the associate
	 */
	public boolean isAssociate() {
		return associate;
	}

	/**
	 * @param associate
	 *            the associate to set
	 */
	public void setAssociate(boolean associate) {
		this.associate = associate;
	}

	/**
	 * @return the idField
	 */
	public Field getIdField() {
		return idField;
	}

	/**
	 * @param idField
	 *            the idField to set
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
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private String getMethodName(Field field){
		String name = field.getName();
		return "get"+name.substring(0, 1).toUpperCase()+name.substring(1);
	}
	
}
