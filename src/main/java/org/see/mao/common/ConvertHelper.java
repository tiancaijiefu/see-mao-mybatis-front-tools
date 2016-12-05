package org.see.mao.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.collect.Lists;

/**
 * 类型转换器
 * 		Map 	  -> 	Bean
 * 		List<Map> -> 	List<Bean>
 * @author Joshua Wang
 * @date 2016年11月24日
 */
public class ConvertHelper {
	
	/**
	 * Map 	  -> 	Bean
	 * @param map
	 * @param clazz
	 * @return
	 */
	public synchronized static <T> T convert(Map<String,String> map,Class<T> clazz){
		T entity = null;
		try {
			entity = clazz.newInstance();
			BeanUtils.populate(entity, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	/**
	 * List<Map> -> 	List<Bean>
	 * @param list
	 * @param clazz
	 * @return
	 */
	public synchronized static <T> List<T> convert(List<Map<String, String>> list,Class<T> clazz) {
		List<T> result = Lists.newArrayList();
		try {
			if (list != null && list.size() > 0) {
				for (Map<String, String> map : list) {
					T object = clazz.newInstance();
					BeanUtils.populate(object, map);
					result.add(object);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
