package org.see.mao.helpers.sql;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.see.mao.exception.MaoException;
import org.see.mao.helpers.StringHelper;
import org.see.mao.persistence.AnnotationTag;
import org.see.mao.persistence.Table;

/**
 * @author Joshua Wang
 * @date 2016年11月18日
 */
public class MaoSQLProvider {
	
	private static final String FIRST_FRAGMENT_FIX = "#";
	
	private static final String SECOND_FRAGMENT_FIX = "{";
	
	private static final String THIRD_FRAGMENT_FIX = "#{entity.";
	
	public static String[] getSQLFragmentArray(String sql){
		return sql.split(FIRST_FRAGMENT_FIX);
	}
	
	public static String formatSQLFragment(String fragment){
		fragment = fragment.trim();
		if(fragment.startsWith(SECOND_FRAGMENT_FIX)){
			return fragment.replace(SECOND_FRAGMENT_FIX, THIRD_FRAGMENT_FIX);
		}else{
			return fragment;
		}
	}
	
	public static String getSql(String sql){
		String[] sqlFragment = getSQLFragmentArray(sql);
		StringBuilder sqlBuilder = new StringBuilder();
		for(String fragment : sqlFragment){
			sqlBuilder.append(formatSQLFragment(fragment)).append(" ");
		}
		return sqlBuilder.toString();
	}
	
	/**
	 * 通用格式化sql，#{property} -> #{entity.property}
	 * @param params
	 * @return
	 */
	public String createCustomSQL(Map<String, Object> params) {
		String sql = StringHelper.objectToStr(params.get("sql"));
		if (null == sql) {
			throw new MaoException("sql is null.");
		}
		return getSql(sql);
	}
	
	/**
	 * 格式化 自定义修改sql
	 * 		#{property} -> #{entity.property}
	 * 		-Version 控制
	 * @param params
	 * @return
	 */
	public String createCustomUpdateSQL(Map<String, Object> params) {
		String sql = StringHelper.objectToStr(params.get("sql"));
		Object entity = params.get("entity");
		if (null == sql) {
			throw new MaoException("sql is null.");
		}
		sql = getSql(MaoSQLBuilderHelper.formatCustomUpdateSql(entity.getClass(), sql));
		System.err.println("修改原版SQL : " + sql);
		return sql;
	}
	
	/**
	 * 格式化 自定义 插入sql
	 * 		#{property} -> #{entity.property}
	 * 		-Version 控制
	 * @param params
	 * @return
	 */
	public String createCustomInsertSQL(Map<String, Object> params) {
		String sql = StringHelper.objectToStr(params.get("sql"));
		Object entity = params.get("entity");
		if (null == sql) {
			throw new MaoException("sql is null.");
		}
		sql = getSql(MaoSQLBuilderHelper.formatCustomInsertSql(entity.getClass(), sql));
		System.err.println("插入原版SQL : " + sql);
		return sql;
	}
	
	/**
	 * 格式化自动生成插入sql
	 * 		系统自动构建sql
	 * 		#{property} -> #{entity.property}
	 * 		-Version 控制
	 * @param params
	 * @return
	 */
	public String createAutoInsertSQL(Map<String, Object> params){
		Object entity = params.get("entity");
		if (null == entity) {
			throw new MaoException("保存对象为null，请确认!");
		}
		String sql = MaoSQLBuilderHelper.builderAutoInsertSql(entity);
		System.err.println("插入原版SQL : " + sql);
		return sql;
	}
	
	/**
	 * 格式化自动生成修改sql
	 * 		系统自动构建sql
	 * 		#{property} -> #{entity.property}
	 * 		-Version 控制
	 * @param params
	 * @return
	 */
	public String createAutoUpdateSQL(Map<String, Object> params){
		Object entity = params.get("entity");
		if (null == entity) {
			throw new MaoException("修改对象为null，请确认!");
		}
		String sql = MaoSQLBuilderHelper.builderAutoUpdateSql(entity);
		System.err.println("修改原版SQL : " + sql);
		return sql;
	}
	
	/**
	 * 批量插入sql
	 * 		系统自动构建sql
	 * 		#{property} -> #{entity.property}
	 * 		-Version 控制
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String createSaveBatchSQL(Map<String,Object> params){
		String tableName = StringHelper.objectToStr(params.get("tableName"));
		String[] columns = (String[]) params.get("columns");
		String[] fields  = (String[]) params.get("fields");
		List<Object> entities  = (List<Object>) params.get("entities");
		if(tableName == null){
			throw new MaoException("tableName为空或null，请指定要操作的tableName！");
		}
		
		if(columns == null || columns.length == 0){
			throw new MaoException("请指定要操作的列名称！");
		}
		
		if(fields == null || fields.length == 0){
			throw new MaoException("请指定要操作的列对应的属性名称！");
		}
		
		if(columns.length != fields.length){
			throw new MaoException("columns数量与fields数量不一致，请确认！");
		}
		
		if(entities == null || entities.isEmpty()){
			throw new MaoException("批量操作集合List为null或长度为0！");
		}
		
		StringBuilder sqlBuilder = new StringBuilder("insert into ").append(tableName).append("(");
		StringBuilder msfBuilder = new StringBuilder("(");
		int len = columns.length;
		for(int i=0;i<len;i++){
			String col = columns[i];
			if(i != 0){
				sqlBuilder.append(",");
				msfBuilder.append(",");
			}
			sqlBuilder.append(col);
			msfBuilder.append("#'{'entities[{0}].").append(fields[i]).append("}");
		}
		Class<?> clazz = entities.get(0).getClass();
		Table table = AnnotationTag.getTable(clazz);
		boolean version = false;
		if(null != table){
			version = table.version();
		}
		//版本控制信息
		if(version){
			sqlBuilder.append(",create_user,update_user,create_date,update_date");
			msfBuilder.append(",#'{'entities[{0}].createUser},#'{'entities[{0}].updateUser},#'{'entities[{0}].createTime},#'{'entities[{0}].updateTime}");
		}
		sqlBuilder.append(") values");
		msfBuilder.append(")");
		MessageFormat msf = new MessageFormat(msfBuilder.toString());
		len = entities.size();
		for(int i = 0;i < len; i++){
			sqlBuilder.append(msf.format(new Object[]{i}));
			if (i < len-1) {
				sqlBuilder.append(",");
            }
		}
		String sql = sqlBuilder.toString();
		System.err.println("批量插入原版SQL : " + sql);
		return sql;
	}
	
	public String createCustomQuerySQLById(Map<String, Object> params) {
		String sql = StringHelper.objectToStr(params.get("sql"));
		if (null == sql) {
			throw new MaoException("sql is null.");
		}
		sql = sql.replaceAll("#\\{.*?\\}", "#{id}");
		return sql;
	}
	
	/**
	 * 创建查询单一对象SQL方法名称
	 * 		根据Class中定义的Annotation生成SQL
	 * @param params
	 * @return
	 */
	public String createQueryAutoSQL(Map<String, Object> params){
		Class<?> clazz = (Class<?>) params.get("clazz");
		Table table = AnnotationTag.getTable(clazz);
		String tableName = table.name();
		if(StringHelper.isBlank(tableName)){
			throw new MaoException(clazz.getName()+"没有设置annotation @Table的名称(name)!");
		}
		return MaoSQLBuilderHelper.builderAutoQuerySql(clazz);
	}
	
	public String createDeleteSQL(Map<String, Object> params){
		Class<?> clazz = (Class<?>) params.get("clazz");
		Table table = AnnotationTag.getTable(clazz);
		String tableName = table.name();
		if(StringHelper.isBlank(tableName)){
			throw new MaoException(clazz.getName()+"没有设置annotation @Table的名称(name)!");
		}
		String sql = "delete from "+tableName+" where id=#{id}";
		return sql;
	}

}
