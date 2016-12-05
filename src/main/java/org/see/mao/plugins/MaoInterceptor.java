package org.see.mao.plugins;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.see.mao.MaoExecutor;
import org.see.mao.PagingParametersFinder;
import org.see.mao.common.CountHelper;
import org.see.mao.common.proxy.ProxyHelper;
import org.see.mao.dto.SeeMetaData;
import org.see.mao.dto.SeePaginationList;
import org.see.mao.dto.datatables.PagingCriteria;

/**
 * @author Joshua Wang
 * @date 2016年11月17日
 */
@Intercepts({
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MaoInterceptor extends CustomerPluginSuper implements Interceptor, Serializable{
	/** serial Version */
	private static final long serialVersionUID = 1L;
	
	/**
	 * perform paging intercetion.
	 *
	 * @param queryArgs
	 *            Executor.query params.
	 */
	@SuppressWarnings("unchecked")
	public Object pageQueryProcess(Invocation invocation) throws Throwable {
		//执行的查询参数
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		final Object parameter = queryArgs[1];
		final RowBounds oldRow = (RowBounds) queryArgs[2];
		//获取元数据
		// the need for paging intercept.
		boolean interceptor = ms.getId().matches(_query_sql_regex);
		// obtain paging information.
		final PagingCriteria pageRequest = interceptor ? PagingParametersFinder.instance.findCriteria(parameter)
				: PagingCriteria.getDefaultCriteria();
		//置分页参数
		final RowBounds rowBounds = (interceptor) ? offset_paging(oldRow, pageRequest) : oldRow;
		int offset = rowBounds.getOffset();
		int limit = rowBounds.getLimit();
		int count = 0;
		//执行的返回对象
		Object obj = null;
		if (_dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
			final BoundSql boundSql = ms.getBoundSql(parameter);
			//原始sql
			String sql = boundSql.getSql().trim();
			Connection connection = null;
			try {
				// get connection
				connection = ms.getConfiguration().getEnvironment().getDataSource().getConnection();
				count = CountHelper.getCount(sql, connection, ms, parameter, boundSql, _dialect);
				PAGINATION_TOTAL.set(count);
			} catch (SQLException e) {
				log.error("The total number of access to the database failure.", e);
			} finally {
				try {
					if (connection != null && !connection.isClosed()) {
						connection.close();
					}
				} catch (SQLException e) {
					log.error("Close the database connection error.", e);
				}
			}
			String new_sql = sortAndFilterSql(sql, pageRequest);
			if (_dialect.supportsLimit()) {
				new_sql = _dialect.getLimitString(new_sql, offset, limit);
				offset = RowBounds.NO_ROW_OFFSET;
			} else {
				new_sql = _dialect.getLimitString(new_sql, 0, limit);
			}
			if (log.isDebugEnabled()) {
				log.debug("pagination sql is :[" + new_sql + "]");
			}
			limit = RowBounds.NO_ROW_LIMIT;
			queryArgs[2] = new RowBounds(offset, limit);
			BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, new_sql);
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
			PAGE_REQUEST.set(pageRequest);
			obj = invocation.proceed();
			SeePaginationList<SeeMetaData> list = (SeePaginationList<SeeMetaData>) obj;
			//pageNo > 1时，且列表中只有一条数据，处理执行删除数据后回显列表
			if(list == null || list.size()==0){//如查询列表为空，则查询上一页
				int pageNo = pageRequest.getPageNumber();
				if(pageNo > 1){
					queryArgs[MAPPED_STATEMENT_INDEX] = ms;
					queryArgs[2] = oldRow;
					obj = query4Page(invocation,pageRequest,count);
				}
			}
		}
		return obj;
	}
	
	/**
	 * perform paging intercetion.
	 *
	 * @param queryArgs
	 *            Executor.update -> update params.
	 */
	private Object updateProcessIntercept(Invocation invocation) throws Throwable {
//		final Object[] updateArgs = invocation.getArgs();
//		final MappedStatement ms = (MappedStatement) updateArgs[MAPPED_STATEMENT_INDEX];
//		final Object parameter = updateArgs[1];
//		final BoundSql boundSql = ms.getBoundSql(parameter);
//		String sql = boundSql.getSql();
//		//格式化sql
//		BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
//		if (log.isDebugEnabled()) {
//			log.debug("==> originalSql: " + newBoundSql);
//		}
//		MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
//		updateArgs[MAPPED_STATEMENT_INDEX] = newMs;
		return invocation.proceed();
	}
	
	/**
	 * perform paging intercetion.
	 *
	 * @param queryArgs
	 *            Executor.update -> save params.
	 */
	private Object saveProcessIntercept(Invocation invocation) throws Throwable {
//		final Object[] updateArgs = invocation.getArgs();
//		final MappedStatement ms = (MappedStatement) updateArgs[MAPPED_STATEMENT_INDEX];
//		final Object parameter = updateArgs[1];
//		final BoundSql boundSql = ms.getBoundSql(parameter);
//		String boundSqlStr = boundSql.getSql();
//		//SeeWrapper seeWrapper = getRequestSeeWrapper(updateArgs);
//		String prefix = "";
//		//格式化sql
//		BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, prefix + boundSqlStr);
//		if (log.isDebugEnabled()) {
//			log.debug("==> originalSql: " + newBoundSql);
//		}
//		MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
//		updateArgs[MAPPED_STATEMENT_INDEX] = newMs;
		return invocation.proceed();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
		boolean isInvoke = false;
		Object object = null;
		// 拦截查询方法
		boolean queryInterceptor = ms.getId().matches(_query_sql_regex);
		if (queryInterceptor) {
			object = pageQueryProcess(invocation);
			isInvoke = true;
		}
		
		// 拦截修改方法
		boolean updateInterceptor = ms.getId().matches(_update_sql_regex);
		if (updateInterceptor) {
			object = updateProcessIntercept(invocation);
			isInvoke = true;
		}
		
		// 拦截保存方法
		boolean saveInterceptor = ms.getId().matches(_save_sql_regex);
		if (saveInterceptor) {
			object = saveProcessIntercept(invocation);
			isInvoke = true;
		}
		
		//拦截删除方法
		boolean deleteInterceptor = ms.getId().matches(_delete_sql_regex);
		if (deleteInterceptor) {
			//return null;//deleteProcessIntercept(invocation);
		}
		
		if(!isInvoke){
			object = invocation.proceed();
		}
		
		if(object != null){
			object = ProxyHelper.proxy(object);
		}
		//拦截 关键字ByPage、update、save、delete关键字外的所有其他方法
		return object;//allProcessIntercept(invocation);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object obj) {
		if (Executor.class.isAssignableFrom(obj.getClass())) {
			return Plugin.wrap(new MaoExecutor((Executor) obj), this);
		}
		return Plugin.wrap(obj, this);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p) {
		super.configProperties(p);
	}
	
}
