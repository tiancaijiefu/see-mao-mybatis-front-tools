package org.see.mao.plugins;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.RowBounds;
import org.see.mao.dialect.DBMS;
import org.see.mao.dialect.Dialect;
import org.see.mao.dialect.DialectClient;
import org.see.mao.dto.SeeMetaData;
import org.see.mao.dto.SeeWrapper;
import org.see.mao.dto.datatables.PagingCriteria;
import org.see.mao.dto.datatables.SearchField;
import org.see.mao.dto.datatables.SortField;
import org.see.mao.helpers.CountHelper;
import org.see.mao.helpers.StringHelper;
import org.see.mao.helpers.reflex.Reflections;
import org.see.mao.helpers.sql.SqlRemoveHelper;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Joshua Wang
 * @date 2016年11月23日
 */
@SuppressWarnings("unchecked")
public abstract class SeeInterceptor {
	
	/** logging */
	protected static final Log log = LogFactory.getLog(SeeInterceptor.class);
	
	/**总记录数线程*/
	protected static final ThreadLocal<Integer> PAGINATION_TOTAL = new ThreadLocal<Integer>();
	/**分页标准对象线程*/
	protected static final ThreadLocal<PagingCriteria> PAGE_REQUEST = new ThreadLocal<PagingCriteria>();
	/**mapped statement parameter index*/
	protected static final int MAPPED_STATEMENT_INDEX = 0;
	
	/** DataBase dialect. */
	protected Dialect _dialect;
	
	/** sql id , in the mapper xml file. */
	public static String _query_sql_regex = "[*]";
	public static String _update_sql_regex = "[*]";
	public static String _save_sql_regex = "[*]";
	public static String _delete_sql_regex = "[*]";
	
	/**
	 * Gets pagination total.
	 * @return the pagination total
	 */
	public static int getPaginationTotal() {
		if (PAGINATION_TOTAL.get() == null) {
			return 0;
		}
		return PAGINATION_TOTAL.get();
	}

	/**
	 * Gets page request.
	 * @return the page request
	 */
	public static PagingCriteria getPageRequest() {
		return PAGE_REQUEST.get();
	}

	/** clear total context. */
	public static void clean() {
		PAGE_REQUEST.remove();
		PAGINATION_TOTAL.remove();
	}
	
	/**
	 * Set the paging information,to RowBuounds.
	 * @param rowBounds rowBounds.
	 * @return 			rowBounds.
	 */
	protected static RowBounds offset_paging(RowBounds rowBounds, PagingCriteria pageRequest) {
		// rowBuounds has offset.
		if (rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET) {
			if (pageRequest != null) {
				return new RowBounds(pageRequest.getDisplayStart(), pageRequest.getDisplaySize());
			}
		}
		return rowBounds;
	}

	/**
	 * 拷贝BoundSql，重置sql
	 * @param ms 		the ms
	 * @param boundSql 	the bound sql
	 * @param sql 		the sql
	 * @return the 		bound sql
	 */
	protected static BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}
	
	// see: MapperBuilderAssistant
	protected static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource sqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource,ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		String[] keyProperties = ms.getKeyProperties();
		builder.keyProperty(keyProperties == null ? null : keyProperties[0]);
		// setStatementTimeout()
		builder.timeout(ms.getTimeout());
		// setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());
		// setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		// setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());
		return builder.build();
	}
	
	/**
	 * Sort and filter sql.
	 * @param sql 				the sql
	 * @param pagingCriteria 	the paging criteria
	 * @return 					the string
	 */
	protected static String sortAndFilterSql(String sql, PagingCriteria pagingCriteria) {
		boolean order = SqlRemoveHelper.containOrder(sql);
		final List<SearchField> searchFields = pagingCriteria.getSearchFields();
		if (searchFields != null && !searchFields.isEmpty()) {
			List<String> where_field = Lists.newArrayList();
			for (SearchField searchField : searchFields) {
				// fix inject sql
				where_field.add(searchField.getField() + StringHelper.LIKE_CHAR + StringHelper.likeValue(searchField.getValue()));
			}
			boolean where = SqlRemoveHelper.containWhere(sql);
			String orderBy = StringHelper.EMPTY;
			if (order) {
				String[] sqls = sql.split(SqlRemoveHelper.ORDER_REGEX);
				sql = sqls[0];
				orderBy = CountHelper.SQL_ORDER + sqls[1];
			}
			sql = String.format((where ? CountHelper.OR_SQL_FORMAT : CountHelper.WHERE_SQL_FORMAT), sql,
					Joiner.on(CountHelper.OR_JOINER).skipNulls().join(where_field), orderBy);
		}

		final List<SortField> sortFields = pagingCriteria.getSortFields();
		if (sortFields != null && !sortFields.isEmpty()) {
			List<String> field_order = Lists.newArrayList();
			for (SortField sortField : sortFields) {
				field_order
						.add(sortField.getField() + StringHelper.BLANK_CHAR + sortField.getDirection().getDirection());
			}
			return String.format(order ? CountHelper.SQL_FORMAT : CountHelper.ORDER_SQL_FORMAT, sql,
					Joiner.on(StringHelper.DOT_CHAR).skipNulls().join(field_order));
		}

		return sql;
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
	
	protected static SeeWrapper getRequestSeeWrapper(Object[] invocationArgs) {
		Object parameter = invocationArgs[1];
		SeeWrapper seeWrapper = new SeeWrapper();
		SeeMetaData seeMetaData = null;
		if (SeeMetaData.class.isAssignableFrom(parameter.getClass())) {
			seeMetaData = (SeeMetaData) parameter;
			seeWrapper.setSeeMetaData(seeMetaData);
		} else if(Map.class.isAssignableFrom(parameter.getClass())) {
			Map<String, Object> paramMap = (Map<String, Object>) parameter;
			if (paramMap != null) {
				Set<String> set = paramMap.keySet();
				for (String key : set) {
					Object val = paramMap.get(key);
					if(val == null){
						continue;
					}
					if (SeeMetaData.class.isAssignableFrom(val.getClass())) {
						seeMetaData = (SeeMetaData) val;
						seeWrapper.setSeeMetaData(seeMetaData);
						break;
					}
					if(List.class.isAssignableFrom(val.getClass())){
						seeWrapper.setSeeMetaDataList((List<SeeMetaData>)val);
						break;
					}
				}
			}
		}
		return seeWrapper;
	}
	
	/**
	 * 处理pageNumber大于1，但数据列表长度为0(查询上一页)
	 * @param invocation
	 * @param pageRequest
	 * @param count
	 * @return
	 * @throws Throwable
	 */
	protected Object query4Page(Invocation invocation,PagingCriteria pageRequest,int count) throws Throwable {
		//执行的查询参数
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		final Object parameter = queryArgs[1];
		final RowBounds oldRow = (RowBounds) queryArgs[2];
		// the need for paging intercept.
		boolean interceptor = ms.getId().matches(_query_sql_regex);
		//重置分页参数
		int pageNo = pageRequest.getPageNumber() - 1;
		int pageSize = pageRequest.getDisplaySize();
		int displayStart = pageNo * pageSize - pageSize;
		pageRequest = PagingCriteria.createCriteria(pageSize,displayStart, pageNo);
		//置分页参数
		final RowBounds rowBounds = (interceptor) ? offset_paging(oldRow, pageRequest) : oldRow;
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		final BoundSql boundSql = ms.getBoundSql(parameter);
		//原始sql
		String sql = boundSql.getSql().trim();
		String prefix = "";
		PAGINATION_TOTAL.set(count);
		String new_sql = sortAndFilterSql(sql, pageRequest);
		new_sql = _dialect.getLimitString(new_sql, offset, limit);
		offset = RowBounds.NO_ROW_OFFSET;
		if (log.isDebugEnabled()) {
			log.debug("pagination sql is :[" + new_sql + "]");
		}
		limit = RowBounds.NO_ROW_LIMIT;
		queryArgs[2] = new RowBounds(offset, limit);
		BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, prefix + new_sql);
		MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
		queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
		PAGE_REQUEST.set(pageRequest);
		return invocation.proceed();
	}
	
	protected void configProperties(Properties p){
		String dialectClass = p.getProperty("dialectClass");
		DBMS dbms;
		if (StringHelper.isEmpty(dialectClass)) {
			String dialect = p.getProperty("dbms");
			Preconditions.checkArgument(!StringHelper.isEmpty(dialect), "dialect property is not found!");
			dbms = DBMS.valueOf(dialect.toUpperCase());
			Preconditions.checkNotNull(dbms, "plugin not super on this database.");
		} else {
			Dialect dialect1 = (Dialect) Reflections.instance(dialectClass);
			Preconditions.checkNotNull(dialect1, "dialectClass is not found!");
			DialectClient.putEx(dialect1);
			dbms = DBMS.EX;
		}

		_dialect = DialectClient.getDbmsDialect(dbms);

		String query_sql_regex = p.getProperty("querySqlRegex");
		if (!StringHelper.isEmpty(query_sql_regex)) {
			_query_sql_regex = query_sql_regex;
		}

		String update_sql_regex = p.getProperty("updateSqlRegex");
		if (!StringHelper.isEmpty(update_sql_regex)) {
			_update_sql_regex = update_sql_regex;
		}

		String save_sql_regex = p.getProperty("saveSqlRegex");
		if (!StringHelper.isEmpty(save_sql_regex)) {
			_save_sql_regex = save_sql_regex;
		}

		String delete_sql_regex = p.getProperty("deleteSqlRegex");
		if (!StringHelper.isEmpty(delete_sql_regex)) {
			_delete_sql_regex = delete_sql_regex;
		}
		clean();
	}
	
	/**
	 * 执行分页查询
	 * 		invocation.proceed();
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	public abstract Object pageQueryProcess(Invocation invocation) throws Throwable;
	
}
