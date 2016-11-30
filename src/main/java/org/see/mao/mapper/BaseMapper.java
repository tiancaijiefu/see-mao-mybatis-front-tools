package org.see.mao.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.see.mao.dto.SeeMetaData;
import org.see.mao.dto.SeePaginationList;
import org.see.mao.dto.datatables.PagingCriteria;
import org.see.mao.helpers.sql.MaoSQLProvider;

/**
 * <p>
 * 数据访问接口
 * </p>
 * 
 * @author Joshua Wang
 * @date 2016年11月17日
 */
public interface BaseMapper<T extends SeeMetaData> extends MapperConfiguration {
	
	/**
	 * 获取实体对象
	 * @param clazz
	 * @param id
	 * @return Object
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = QUERY_AUTO_SQL_DEVICE)
	public T get(@Param("clazz") Class<T> clazz,@Param("id") Serializable id);
	
	/**
	 * 根据一个参数获取唯一目标数据
	 * 		param通常为id，但也可以尝试使用其他可以确定唯一一条数据的条件
	 * @param param 查询标识 sql中能确定唯一数据的条件
	 * @param sql
	 * @return
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = QUERY_ID_SQL_DEVICE)
	public T getOne(@Param("id") Serializable param, @Param("sql") String sql);
	
	/**
	 * 根据实体查询对象
	 * 
	 * @param entity
	 * @param sql
	 * @return
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public T getOneByEntity(@Param("entity") T entity,@Param("sql") String sql);
	
	/**
	 * 根据一个参数获取目标数据集合
	 * @param param	查询标识 sql中能确定目标数据集合的条件
	 * @param sql
	 * @return
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = QUERY_ID_SQL_DEVICE)
	public List<T> listByParam(@Param("id")Serializable param,@Param("sql") String sql);
	
	/**
	 * 查询列表通用方法
	 * 
	 * @param entity
	 * @param sql
	 * @return
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public List<T> listByEntity(@Param("entity") T entity, @Param("sql") String sql);

	/**
	 * 分页列表
	 * @param user
	 * @param pagingCriteria
	 * @return
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	SeePaginationList<T> listByPage(PagingCriteria pagingCriteria,@Param("entity") T entity, @Param("sql") String sql);
	
	/**
	 * 保存对象
	 * 		根据@Table - version配置决定是否配置版本控制信息(create.date/user  update.date/user)
	 * @param entity
	 * @return
	 */
	@InsertProvider(type = MaoSQLProvider.class, method = AUTO_INSERT_SQL_DEVICE)
	public int save(@Param("entity") T entity);
	
	/**
	 * 保存对象通用方法
	 * 		根据@Table - version配置决定是否配置版本控制信息(create.date/user  update.date/user)
	 * @param entity
	 * @param sql
	 * @return
	 */
	@InsertProvider(type = MaoSQLProvider.class, method = CUSTOM_INSERT_SQL_DEVICE)
	public int saveEntity(@Param("entity") T entity, @Param("sql") String sql);
	
	/**
	 * 批量保存对象通用方法
	 * 		根据@Table - version配置决定是否配置版本控制信息(create.date/user  update.date/user)
	 * @param entities
	 * @param tableName
	 * @param columns
	 * @return
	 */
	@InsertProvider(type = MaoSQLProvider.class, method = BATCH_INSERT_SQL_DEVICE)
	public int saveEntities(@Param("entities") List<T> entities, @Param("tableName") String tableName, @Param("columns") String[] columns, @Param("fields") String[] fields);
	
	/**
	 * 
	 * @param clazz	要操作的类，此类中必须定义注解为Table并指定映射的数据库表名
	 * @param id	要删除的数据库中的ID
	 * @return
	 */
	@DeleteProvider(type = MaoSQLProvider.class, method = DELETE_SQL_DEVICE)
	public int delete(@Param("clazz") Class<?> clazz,@Param("id") Serializable id);
	
	/**
	 * 根据SQL删除数据
	 * 		注：为防止SQL注入，多用于非用户操作删除，参数值为手动拼装
	 * 		例：delete from t_user where id=1/id=1 and flag='2'/......
	 * @param id
	 * @param sql
	 * @return
	 */
	@DeleteProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public int deleteBySql(@Param("sql") String sql);

	/**
	 * 根据对象参数和SQL删除
	 * 
	 * @param entity
	 * @param sql
	 * @return
	 */
	@DeleteProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public int deleteByEntityAndSql(@Param("entity") T entity, @Param("sql") String sql);

	/**
	 * 保存修改对象
	 * 		根据@Table - version配置决定是否配置版本控制信息(create.date/user  update.date/user)
	 * @param entity
	 * @return
	 */
	@UpdateProvider(type = MaoSQLProvider.class, method = AUTO_UPDATE_SQL_DEVICE)
	public int update(@Param("entity") T entity);
	
	/**
	 * 根据sql通用修改方法，此方法忽略Version控制
	 * 		如需要控制版本信息（Version）请调用update/updateEntity方法
	 * @param entity
	 * @param sql
	 * @return
	 */
	@UpdateProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public int updateBySql(@Param("sql") String sql);

	/**
	 * 修改对象通用方法
	 * 
	 * @param entity
	 * @param sql
	 * @return
	 */
	@UpdateProvider(type = MaoSQLProvider.class, method = CUSTOM_UPDATE_SQL_DEVICE)
	public int updateEntity(@Param("entity") T entity, @Param("sql") String sql);
	
	//===============================非泛型操作区域(java.util.Map)============================================
	/**
	 * 根据一个参数获取目标数据的Map数据
	 * 		param通常为id，但也可以尝试使用其他可以确定唯一一条数据的条件
	 * @param param 查询标识 sql中能确定唯一数据的条件
	 * @param sql
	 * @return java.util.List<Map<String,String>;
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = QUERY_ID_SQL_DEVICE)
	public Map<String,String> getMap(@Param("id")Serializable param,@Param("sql") String sql);
	
	/**
	 * 根据一个参数获取目标数据的Map格式列表
	 * @param param	查询标识 sql中能确定目标数据集合的条件
	 * @param sql
	 * @return java.util.List<Map<String,String>;
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = QUERY_ID_SQL_DEVICE)
	public List<Map<String,String>> getMapsByParam(@Param("id")Serializable param,@Param("sql") String sql);
	
	/**
	 * 根据entity获取一条数据
	 * @param entity
	 * @param sql
	 * @return java.util.List<Map<String,String>;
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public Map<String,String> getMapByEntity(@Param("entity") SeeMetaData entity, @Param("sql") String sql);
	
	/**
	 * 根据entity查询结果集（Map）
	 * @param entity
	 * @param sql
	 * @return	java.util.List<Map<String,String>;
	 */
	@SelectProvider(type = MaoSQLProvider.class, method = CUSTOM_SQL_DEVICE)
	public List<Map<String,String>> getMapsByEntity(@Param("entity") SeeMetaData entity, @Param("sql") String sql);
	
}
