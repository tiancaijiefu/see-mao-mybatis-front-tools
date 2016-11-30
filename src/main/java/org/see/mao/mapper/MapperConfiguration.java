package org.see.mao.mapper;

/**
 * 上层Mapper接口
 * 
 * @author Joshua Wang
 * @date 2016年11月21日
 */
public interface MapperConfiguration {

	// ===================================通用方法名称定义==================================================================
	/**
	 * 通用SQL格式化方法名称
	 */
	public final static String CUSTOM_SQL_DEVICE = "createCustomSQL";

	// ===================================save方法名称定义==================================================================
	/**
	 * 格式化 自定义 插入sql 方法名称
	 */
	public final static String CUSTOM_INSERT_SQL_DEVICE = "createCustomInsertSQL";

	/**
	 * 格式化 自动构建 插入sql 方法名称
	 */
	public final static String AUTO_INSERT_SQL_DEVICE = "createAutoInsertSQL";

	/**
	 * 创建批量插入SQL方法名称
	 */
	public final static String BATCH_INSERT_SQL_DEVICE = "createSaveBatchSQL";

	// ===================================update方法名称定义==================================================================

	/**
	 * 格式化 自动构建 修改sql 方法名称
	 */
	public final static String AUTO_UPDATE_SQL_DEVICE = "createAutoUpdateSQL";

	/**
	 * 格式化 自定义 修改sql 方法名称
	 */
	public final static String CUSTOM_UPDATE_SQL_DEVICE = "createCustomUpdateSQL";

	// ===================================select方法名称定义==================================================================
	/**
	 * 创建查询单一对象SQL方法名称
	 */
	public final static String QUERY_ID_SQL_DEVICE = "createCustomQuerySQLById";

	/**
	 * 创建查询单一对象SQL方法名称 根据Class中定义的Annotation生成SQL
	 */
	public final static String QUERY_AUTO_SQL_DEVICE = "createQueryAutoSQL";

	// ===================================delete方法名称定义==================================================================
	/**
	 * 创建删除SQL方法名称
	 */
	public final static String DELETE_SQL_DEVICE = "createDeleteSQL";

}
