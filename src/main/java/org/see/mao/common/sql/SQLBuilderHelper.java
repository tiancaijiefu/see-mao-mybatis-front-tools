package org.see.mao.common.sql;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.see.mao.common.reflex.AnnotationReflections;
import org.see.mao.common.sql.build.SelectBuilder;
import org.see.mao.persistence.AnnotationTag;
import org.see.mao.persistence.MetaDataAnnotationConfig;
import org.see.mao.persistence.SQLBuilderConfig;
import org.see.mao.persistence.Table;
import org.see.mao.persistence.VersionConfig;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * 
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class SQLBuilderHelper {

	/** sqlConfig's cache */
	private static final Map<Class<?>, SQLBuilderConfig> sqlCache = new ConcurrentReferenceHashMap<Class<?>, SQLBuilderConfig>(256);

	/** version's info */
	static String updateTime = VersionConfig.UPDATE_TIME;
	static String updateUser = VersionConfig.UPDATE_USER;
	static String createTime = VersionConfig.CREATE_TIME;
	static String createUser = VersionConfig.CREATE_USERE;

	/**
	 * 格式化客户端自定义SQL(控制版本信息)
	 * 
	 * @param clazz
	 * @return
	 */
	public synchronized static String formatCustomUpdateSql(Class<?> clazz, String sql) {
		Table table = AnnotationTag.getTable(clazz);
		boolean version = false;
		if(table != null){
			version = table.version();
		}
		// the version - true
		if (version) {
			String[] sqls = sql.split("where");
			String prefixSql = sqls[0];
			String suffixSql = sqls[1];
			// 定义新SQL
			StringBuilder sqlBuilder = new StringBuilder(prefixSql);
			if (!prefixSql.toLowerCase().matches(".*," + updateTime + ".*") && !prefixSql.toLowerCase().matches(".* " + updateTime + ".*")) {
				sqlBuilder.append("," + updateTime + "=").append("#{updateTime}");
			}
			if (!prefixSql.toLowerCase().matches(".*," + updateUser + ".*") && !prefixSql.toLowerCase().matches(".* " + updateUser + ".*")) {
				sqlBuilder.append("," + updateUser + "=").append("#{updateUser}");
			}
			sqlBuilder.append(" where ").append(suffixSql);
			sqlBuilder.append(" and " + updateTime + "=").append("#{version}");
			sql = sqlBuilder.toString();
		}
		return sql;
	}
	
	/**
	 * 格式化客户端自定义SQL(控制版本信息)
	 * 
	 * @param clazz
	 * @return
	 */
	public synchronized static String formatCustomInsertSql(Class<?> clazz, String sql) {
		Table table = AnnotationTag.getTable(clazz);
		boolean version = false;
		if(table != null){
			version = table.version();
		}
		// the @Version's settings to true
		if (version) {
			int prefixIndex = sql.indexOf(")");
			int suffixIndex = sql.lastIndexOf(")");
			String prefixSql = sql.substring(0, prefixIndex);
			String suffixSql = sql.substring(prefixIndex + 1, suffixIndex);
			// 定义新SQL
			StringBuilder prefixBuilder = new StringBuilder(prefixSql);
			StringBuilder suffixBuilder = new StringBuilder();
			/** 修改时间、修改人、创建时间、创建人 是否通过拦截器拼接 默认false */
			boolean update_date_flag = false, update_user_flag = false, create_date_flag = false,
					create_user_flag = false;
			if (!prefixSql.toLowerCase().matches(".*," + updateTime + ".*")
					&& !prefixSql.toLowerCase().matches(".* " + updateTime + ".*")) {
				prefixBuilder.append("," + updateTime + "");
				update_date_flag = true;
			}
			
			if (!prefixSql.toLowerCase().matches(".*," + updateUser + ".*")
					&& !prefixSql.toLowerCase().matches(".* " + updateUser + ".*")) {
				prefixBuilder.append("," + updateUser + "");
				update_user_flag = true;
			}
			
			if (!prefixSql.toLowerCase().matches(".*," + createTime + ".*")
					&& !prefixSql.toLowerCase().matches(".* " + createTime + ".*")) {
				prefixBuilder.append("," + createTime + "");
				create_date_flag = true;
			}
			if (!prefixSql.toLowerCase().matches(".*," + createUser + ".*")
					&& !prefixSql.toLowerCase().matches(".* " + createUser + ".*")) {
				prefixBuilder.append("," + createUser + "");
				create_user_flag = true;
			}
			prefixBuilder.append(") ");
			
			// values
			suffixBuilder.append(suffixSql);
			if (update_date_flag) {
				suffixBuilder.append(",#{updateTime}");
			}
			if (update_user_flag) {
				suffixBuilder.append(",#{updateUser}");
			}
			if (create_date_flag) {
				suffixBuilder.append(",#{createTime}");
			}
			if (create_user_flag) {
				suffixBuilder.append(",#{createUser}");
			}
			suffixBuilder.append(")");
			prefixBuilder.append(suffixBuilder);
			sql = prefixBuilder.toString();
		}
		return sql;
	}

	/**
	 * 创建插入语句
	 * 
	 * @param clazz
	 * @return
	 */
	public synchronized static String builderAutoInsertSql(Object object) {
		Class<?> clazz = object.getClass();
		//get cache's sqlConfig
		SQLBuilderConfig sqlConfig = sqlCache.get(clazz);
		String sql = sqlConfig!=null?sqlConfig.getAutoSaveSql():null;
		if (sql == null) {
			sqlConfig = sqlConfig!=null?sqlConfig:new SQLBuilderConfig();
			StringBuilder sqlBuilder = new StringBuilder("insert into ");
			StringBuilder valBuilder = new StringBuilder(" values(");
			MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
			// tableName
			sqlBuilder.append(annotationConfig.getTableName()).append("(");
			// id
			Field idField = annotationConfig.getIdField();
			if(null != annotationConfig.getIdField()){
				Object id = null;
				boolean isAccess = idField.isAccessible();
				if(!isAccess){
					idField.setAccessible(true);
					try {
						id = idField.get(object);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				idField.setAccessible(isAccess);
				if(null != id){
					sqlBuilder.append("id,");
					valBuilder.append("#{entity.id},");
				}
			}
			// columns
			List<Field> fieldList = annotationConfig.getColumnFields();
			int len = fieldList.size();
			for (int i = 0; i < len; i++) {
				Field field = fieldList.get(i);
				if (i != 0) {
					sqlBuilder.append(",");
					valBuilder.append(",");
				}
				sqlBuilder.append(AnnotationTag.getColumn(field));
				valBuilder.append("#{entity.").append(field.getName()).append("}");
			}
			//version
			boolean isVersion = annotationConfig.isVersion();
			if (isVersion) {
				sqlBuilder.append("," + createUser + "," + createTime + "," + updateUser + "," + updateTime + "");
				valBuilder.append(",#{entity.createUser},#{entity.createTime},#{entity.updateUser},#{entity.updateTime}");
			}
			sqlBuilder.append(")");
			valBuilder.append(")");
			sqlBuilder.append(valBuilder);
			sqlConfig.setAutoSaveSql(sqlBuilder.toString());
			//cache
			sqlCache.put(clazz, sqlConfig);
		}
		return sqlConfig.getAutoSaveSql();
	}
	
	/**
	 * 创建修改语句
	 * 
	 * @param clazz
	 * @return
	 */
	public synchronized static String builderAutoUpdateSql(Object object) {
		Class<?> clazz = object.getClass();
		//get cache's sqlConfig
		SQLBuilderConfig sqlConfig = sqlCache.get(clazz);
		String sql = sqlConfig!=null?sqlConfig.getAutoUpdateSql():null;
		if (sql == null) {
			sqlConfig = sqlConfig!=null?sqlConfig:new SQLBuilderConfig();
			StringBuilder sqlBuilder = new StringBuilder("update ");
			MetaDataAnnotationConfig annotationConfig = AnnotationReflections.getAnnotationConfig(clazz);
			// tableName
			sqlBuilder.append(annotationConfig.getTableName()).append(" set ");
			// columns
			List<Field> fieldList = annotationConfig.getColumnFields();
			int len = fieldList.size();
			for (int i = 0; i < len; i++) {
				Field field = fieldList.get(i);
				if (i != 0) {
					sqlBuilder.append(",");
				}
				sqlBuilder.append(AnnotationTag.getColumn(field)).append("=#{entity.").append(field.getName()).append("}");
			}
			//version
			boolean isVersion = annotationConfig.isVersion();
			if (isVersion) {
				sqlBuilder.append(","+ createUser).append("=#{entity.createUser}");
				sqlBuilder.append(","+ createTime).append("=#{entity.createTime}");
				sqlBuilder.append(","+ updateUser).append("=#{entity.updateUser}");
				sqlBuilder.append(","+ updateTime).append("=#{entity.updateTime}");
			}
			sqlBuilder.append(" where id=#{entity.id}");
			sqlConfig.setAutoUpdateSql(sqlBuilder.toString());
			//cache
			sqlCache.put(clazz, sqlConfig);
		}
		return sqlConfig.getAutoUpdateSql();
	}
	
	/**
	 * 创建查询语句
	 * @param clazz
	 * @return
	 */
	public synchronized static String builderAutoQuerySql(Class<?> clazz) {
		SQLBuilderConfig sqlConfig = sqlCache.get(clazz);
		if(null == sqlConfig){
			sqlConfig = new SQLBuilderConfig();
		}
		String sql = sqlConfig.getAutoQueryOneSql();
		if (sql == null) {
			sql = SelectBuilder.getUseIdSelectSql(clazz);
			sqlConfig.setAutoQueryOneSql(sql);
			sqlCache.put(clazz, sqlConfig);
		}
		return sql;
	}
	
}