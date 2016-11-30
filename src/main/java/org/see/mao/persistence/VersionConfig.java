package org.see.mao.persistence;

/**
 * @author Joshua Wang
 * @date 2016年11月29日
 */
public interface VersionConfig {
	
	/**
	 * 创建时间数据库中字段名称
	 * see平台设计不合理之处，此处应该采用create_time，建议新系统在此处更改为create_time
	 * 		parttern yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String CREATE_TIME = "CREATE_DATE";
	
	/**
	 * 修改时间数据库中字段名称
	 * see平台设计不合理之处，此处应该采用update_time，建议新系统在此处更改为update_time
	 * 		parttern yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String UPDATE_TIME = "UPDATE_DATE";
	
	/**
	 * 创建者id数据库中字段名称
	 */
	public static final String CREATE_USERE = "CREATE_USER";
	
	/**
	 * 修改者id数据库中字段名称
	 */
	public static final String UPDATE_USER = "UPDATE_USER";
	
}
