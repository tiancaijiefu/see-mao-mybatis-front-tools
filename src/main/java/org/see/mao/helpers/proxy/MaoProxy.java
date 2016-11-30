package org.see.mao.helpers.proxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.see.mao.dto.SeeMetaData;
import org.see.mao.exception.MaoException;
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
	
	public <T> T getProxy(SeeMetaData metaData){
		if(metaData == null){
			throw new MaoException("生成代理出错！");
		}
		this.targetClass = metaData.getClass();
		Enhancer enhancer = proxCache.get(targetClass);
		if(enhancer == null){
			enhancer = new Enhancer();
			enhancer.setSuperclass(this.targetClass);
			enhancer.setCallbackFilter(new CallbackFilter() {
				@Override
				public int accept(Method method) {
					OneToOne oneToOne = method.getAnnotation(OneToOne.class);
					OneToMany oneToMany = method.getAnnotation(OneToMany.class);
					if(oneToOne != null || oneToMany != null){
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
			Object o = getProxy((SeeMetaData)entity);
			result.add((T) o);
		}
		return result;
	}
	

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;
		
        result = proxy.invokeSuper(obj, args);
        
        System.out.println(obj+"===================2222222========================="+method);
        
        return result;
	}

}
