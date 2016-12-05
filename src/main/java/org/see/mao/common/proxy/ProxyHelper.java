package org.see.mao.common.proxy;

import java.util.List;

import org.see.mao.common.reflex.AnnotationReflections;
import org.see.mao.dto.MetaData;
import org.see.mao.persistence.MetaDataAnnotationConfig;

/**
 * @author Joshua Wang
 * @date 2016年12月2日
 */
public class ProxyHelper {

	/**
	 * 根据对象生成代理对象
	 * @param object
	 * @return
	 */
	public static Object proxy(Object object){
		Class<?> clazz = object.getClass();
		MetaDataAnnotationConfig config = null;//AnnotationReflectionHelper.getAnnotationConfig(clazz);
		if (MetaData.class.isAssignableFrom(object.getClass())) {
			config = AnnotationReflections.getAnnotationConfig(clazz);
			if(config.isAssociate()){
				object = new MaoProxy().getProxy((MetaData)object);
			}
		}
		if(List.class.isAssignableFrom(clazz)){
			List<?> list = (List<?>) object;
			if(list.size() > 0){
				Object o = list.get(0);
				Class<?>  clazz_ = o.getClass();
				if (MetaData.class.isAssignableFrom(clazz_)) {
					config = AnnotationReflections.getAnnotationConfig(clazz_);
					if(config.isAssociate()){
						object = new MaoProxy().getProxy(list);
					}
				}
			}
		}
		return object;
	}

}
