package org.see.mao.common.proxy;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.see.common.spring.SpringContextHolder;
import org.see.mao.common.ConvertHelper;
import org.see.mao.common.reflex.AnnotationReflections;
import org.see.mao.common.reflex.Reflections;
import org.see.mao.common.sql.SQLBuilderHelper;
import org.see.mao.common.sql.build.SelectBuilder;
import org.see.mao.dto.MetaData;
import org.see.mao.exception.MaoException;
import org.see.mao.mapper.CustomMapper;
import org.see.mao.persistence.AnnotationTag;
import org.see.mao.persistence.MetaDataAnnotationConfig;
import org.see.mao.persistence.OneToMany;
import org.see.mao.persistence.OneToOne;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.google.common.collect.Lists;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * MAO生成代理操作对象 - 基于CGLIB
 * @author Joshua Wang
 * @date 2016年11月29日
 */
@SuppressWarnings("unchecked")
public class MaoProxy implements MethodInterceptor {
	/**target class*/
	private Class<?> targetClass;
	/**advice's type*/
	private final static int LAZY_LOADER = 0;
	private final static int ADVICE = 1;
	/**cache the Enhancer's instance*/
	private static final Map<Class<?>, Enhancer> proxCache = new ConcurrentReferenceHashMap<Class<?>, Enhancer>(256);
	/**mapper*/
	private static final CustomMapper mapper = SpringContextHolder.getBean(CustomMapper.class);
	
	public <T> T getProxy(MetaData metaData){
		if(metaData == null){
			throw new MaoException("生成代理出错！");
		}
		targetClass = metaData.getClass();
		Enhancer enhancer = proxCache.get(targetClass);
		if(enhancer == null){
			MetaDataAnnotationConfig config = AnnotationReflections.getAnnotationConfig(targetClass);
			List<String> associateGetNames = Lists.newArrayList(config.getAssociateGetNames());
			enhancer = new Enhancer();
			enhancer.setSuperclass(this.targetClass);
			enhancer.setCallbackFilter(new CallbackFilter() {
				@Override
				public int accept(Method method) {
					String name = method.getName();
					if(associateGetNames.contains(name)){
						associateGetNames.remove(name);
						return LAZY_LOADER;
					}
					return ADVICE;
				}
			});
			proxCache.put(targetClass, enhancer);
		}
		enhancer.setCallbacks(new Callback[]{
				this,
				new LazyLoader() {
					@Override
					public Object loadObject() throws Exception {
						return metaData;
					}
				}
		});
		return (T) enhancer.create();
	}
	
	public <T> List<T> getProxy(List<T> list){
		if(list == null || list.size() == 0){
			throw new MaoException("生成代理出错！");
		}
		List<T> result = Lists.newArrayList();
		for(T entity : list){
			Object o = getProxy((MetaData)entity);
			result.add((T) o);
		}
		return result;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proceed(obj,method);
	}
	
	public static Object proceed(Object object,Method method){
		//执行返回
		Object result = null;
		Class<?> clazz = Reflections.getUserClass(object.getClass());
		MetaDataAnnotationConfig config = AnnotationReflections.getAnnotationConfig(clazz);
		Field field = config.searchAssociateField(method);
		
		OneToOne  oneToOne  = field.getAnnotation(OneToOne.class);
		OneToMany oneToMany = field.getAnnotation(OneToMany.class);
		if(oneToOne == null && oneToMany == null){
			throw new MaoException(clazz+"的"+field.getName()+"没有设置 @OneToOne 或  @OneToMany");
		}
		if(oneToOne != null && oneToMany != null){
			throw new MaoException(clazz+"的"+field.getName()+"同时设置了 @OneToOne 和 @OneToMany");
		}

		//操作的目标类型
		Class<?> targetClass = null;
		if(oneToOne != null){
			targetClass = oneToOne.targetEntity();
			String mappedFileGetMethodName = AnnotationTag.getMethodName(oneToOne.mappedBy());
			Serializable id = (Serializable) Reflections.invokeMethod(object, mappedFileGetMethodName, null, null);
			String sql = SQLBuilderHelper.builderAutoQuerySql(targetClass);
			result = ConvertHelper.convert(mapper.getMap(id, sql), targetClass);
		}
		
		if(oneToMany != null){
			targetClass = oneToMany.targetEntity();
			boolean inter = oneToMany.interTable();
			Serializable id = (Serializable) Reflections.invokeMethod(object, "getId", null, null);
			String sql = null;
			if(inter){//中间表
				String interTableName = oneToMany.interTableName();
				String refColumnName = oneToMany.refColumnName();
				String inverseColumnName = oneToMany.inverseColumnName();
				// select * from user u where u.id in(select user_id from t_user_role where role_id=#{id})
				// TODO 后续优化为exists
				String columnsStr = SelectBuilder.getColumnsStr(targetClass);
				StringBuilder sqlBuilder = new StringBuilder("select id,");
				sqlBuilder.append(columnsStr);
				sqlBuilder.append(" from ").append(AnnotationTag.getTable(targetClass).name());
				sqlBuilder.append(" where id in (");
				sqlBuilder.append("select "+inverseColumnName+" from "+interTableName+" where "+refColumnName+"=#{id}");
				sqlBuilder.append(")");
				sql = sqlBuilder.toString();
			}else{//无中间表
				String associateColumn = oneToMany.associateColumnName();
				sql = SelectBuilder.getUserWhereColumnSelectSql(targetClass, associateColumn);
			}
			result = ConvertHelper.convert(mapper.getMapsByParam(id, sql), targetClass);
		}
		return ProxyHelper.proxy(result);
	}
	
}
