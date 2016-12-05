package org.see.mao.persistence;

public class SQLBuilderConfig {

	/**
	 * MAO自动生成插入SQL
	 */
	private String autoSaveSql;

	/**
	 * 客户端自定义插入SQL
	 */
	private String customSaveSql;

	/**
	 * MAO自动生成修改SQL
	 */
	private String autoUpdateSql;

	/**
	 * 客户端自定义修改SQL
	 */
	private String customUpdateSql;

	/**
	 * MAO自动生成删除SQL
	 */
	private String autoDeleteSql;

	/**
	 * 客户端自定义删除SQL
	 */
	private String customDeleteSql;

	/**
	 * MAO自动生成查询SQL
	 */
	private String autoQueryOneSql;

	/**
	 * MAO自动生成查询SQL -> OneToOne
	 */
	private String autoQueryOneToOneSql;

	/**
	 * MAO自动生成查询SQL
	 */
	private String customQueryOneSql;

	/**
	 * @return the autoSaveSql
	 */
	public String getAutoSaveSql() {
		return autoSaveSql;
	}

	/**
	 * @param autoSaveSql
	 *            the autoSaveSql to set
	 */
	public void setAutoSaveSql(String autoSaveSql) {
		this.autoSaveSql = autoSaveSql;
	}

	/**
	 * @return the customSaveSql
	 */
	public String getCustomSaveSql() {
		return customSaveSql;
	}

	/**
	 * @param customSaveSql
	 *            the customSaveSql to set
	 */
	public void setCustomSaveSql(String customSaveSql) {
		this.customSaveSql = customSaveSql;
	}

	/**
	 * @return the autoUpdateSql
	 */
	public String getAutoUpdateSql() {
		return autoUpdateSql;
	}

	/**
	 * @param autoUpdateSql
	 *            the autoUpdateSql to set
	 */
	public void setAutoUpdateSql(String autoUpdateSql) {
		this.autoUpdateSql = autoUpdateSql;
	}

	/**
	 * @return the customUpdateSql
	 */
	public String getCustomUpdateSql() {
		return customUpdateSql;
	}

	/**
	 * @param customUpdateSql
	 *            the customUpdateSql to set
	 */
	public void setCustomUpdateSql(String customUpdateSql) {
		this.customUpdateSql = customUpdateSql;
	}

	/**
	 * @return the autoDeleteSql
	 */
	public String getAutoDeleteSql() {
		return autoDeleteSql;
	}

	/**
	 * @param autoDeleteSql
	 *            the autoDeleteSql to set
	 */
	public void setAutoDeleteSql(String autoDeleteSql) {
		this.autoDeleteSql = autoDeleteSql;
	}

	/**
	 * @return the customDeleteSql
	 */
	public String getCustomDeleteSql() {
		return customDeleteSql;
	}

	/**
	 * @param customDeleteSql
	 *            the customDeleteSql to set
	 */
	public void setCustomDeleteSql(String customDeleteSql) {
		this.customDeleteSql = customDeleteSql;
	}

	/**
	 * @return the autoQueryOneSql
	 */
	public String getAutoQueryOneSql() {
		return autoQueryOneSql;
	}

	/**
	 * @param autoQueryOneSql
	 *            the autoQueryOneSql to set
	 */
	public void setAutoQueryOneSql(String autoQueryOneSql) {
		this.autoQueryOneSql = autoQueryOneSql;
	}

	/**
	 * @return the customQueryOneSql
	 */
	public String getCustomQueryOneSql() {
		return customQueryOneSql;
	}

	/**
	 * @param customQueryOneSql
	 *            the customQueryOneSql to set
	 */
	public void setCustomQueryOneSql(String customQueryOneSql) {
		this.customQueryOneSql = customQueryOneSql;
	}

	/**
	 * @return the autoQueryOneToOneSql
	 */
	public String getAutoQueryOneToOneSql() {
		return autoQueryOneToOneSql;
	}

	/**
	 * @param autoQueryOneToOneSql
	 *            the autoQueryOneToOneSql to set
	 */
	public void setAutoQueryOneToOneSql(String autoQueryOneToOneSql) {
		this.autoQueryOneToOneSql = autoQueryOneToOneSql;
	}

}
