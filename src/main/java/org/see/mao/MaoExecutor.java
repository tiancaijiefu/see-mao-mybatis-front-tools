/*
 * Copyright © 2012-2013 mumu@yfyang. All Rights Reserved.
 */

package org.see.mao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.see.mao.dto.PageMyBatis;
import org.see.mao.dto.SeePaginationList;
import org.see.mao.plugins.MaoInterceptor;


/**
 * Paging executor.
 * Dependence of mybatis agent.
 * @author yfyang
 * @author htlu
 * @author Joshua Wang
 * @date 2012-09-09 7:22 PM / 2016年11月21日
 * @since JDK 1.8
 */
@SuppressWarnings("rawtypes")
public class MaoExecutor implements Executor {

    /** logging */
    private static final Log LOG = LogFactory.getLog(MaoExecutor.class);
    /** mybatis executor interface */
    private final Executor executor;

    /**
     * Paging Constructor.
     *
     * @param executor Trim executor.
     */
    public MaoExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
    	int count = executor.update(ms, parameter);
    	if(count == 0){
    		
    	}
        return count;
    }

	@Override
    public <E> List<E> query(MappedStatement ms, Object parameter,RowBounds rowBounds, ResultHandler resultHandler,CacheKey cacheKey, BoundSql boundSql) throws SQLException {

        final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
        int total = MaoInterceptor.getPaginationTotal();
        try {
            if (total != 0) {
                final PageMyBatis<E> result = new PageMyBatis<E>(rows, MaoInterceptor.getPageRequest(), total);
                doCache(ms, result, parameter, rowBounds);
                return result;
            } else {
                return new PageMyBatis<E>(rows);
            }
        } finally {
        	MaoInterceptor.clean();
        }
    }

	@Override
    public <E> List<E> query(MappedStatement ms, Object parameter,RowBounds rowBounds, ResultHandler resultHandler)throws SQLException {
		boolean interceptor = ms.getId().matches(MaoInterceptor._query_sql_regex);
        final List<E> rows = executor.query(ms, parameter, rowBounds, resultHandler);
        int total = MaoInterceptor.getPaginationTotal();
        try {
        	if(interceptor){
        		if (total != 0) {
        			final SeePaginationList<E> result = new SeePaginationList<E>(rows, MaoInterceptor.getPageRequest(), total);
        			doCache(ms, result, parameter, rowBounds);
        			return result;
        		} else {
        			return new SeePaginationList<E>(rows);
        		}
        	}
        	return rows;
        } finally {
        	MaoInterceptor.clean();
        }
    }
    
    /**
     * do mybatis cache with this executor.
     *
     * @param ms        mapped statuement.
     * @param result    database result.
     * @param parameter sql paramater.
     * @param rowBounds row bounds
     * @param <E>       paramter.
     */
    private <E> void doCache(MappedStatement ms, PageMyBatis<E> result, Object parameter, RowBounds rowBounds) {
        // if the current of the executor is for CachingExecutor
        final Cache cache = ms.getCache();
        // Determine whether the current query cache.
        if (executor.getClass().isAssignableFrom(CachingExecutor.class) && cache != null) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            final CacheKey cacheKey = createCacheKey(ms, parameter, rowBounds, boundSql);
            if (LOG.isDebugEnabled()) {
                LOG.debug("cache executor the cache's kye  is " + cacheKey);
            }
            cache.putObject(cacheKey, result);
        }
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return executor.flushStatements();
    }

    @Override
    public void commit(boolean required) throws SQLException {
        executor.commit(required);
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        executor.rollback(required);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject,
                                   RowBounds rowBounds, BoundSql boundSql) {
        return executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return executor.isCached(ms, key);
    }

    @Override
    public void clearLocalCache() {
        executor.clearLocalCache();
    }

    @Override
    public void deferLoad(MappedStatement mappedStatement, MetaObject metaObject,
                          String s, CacheKey cacheKey, Class<?> aClass) {

        executor.deferLoad(mappedStatement, metaObject, s, cacheKey, aClass);
    }

    @Override
    public Transaction getTransaction() {
        return executor.getTransaction();
    }

    @Override
    public void close(boolean forceRollback) {
        //clear
        MaoInterceptor.clean();
        executor.close(forceRollback);
    }

    @Override
    public boolean isClosed() {
        return executor.isClosed();
    }

	/* (non-Javadoc)
	 * @see org.apache.ibatis.executor.Executor#queryCursor(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds)
	 */
	@Override
	public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.executor.Executor#setExecutorWrapper(org.apache.ibatis.executor.Executor)
	 */
	@Override
	public void setExecutorWrapper(Executor executor) {
		// TODO Auto-generated method stub
		
	}

}
