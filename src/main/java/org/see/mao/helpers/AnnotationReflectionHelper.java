package org.see.mao.helpers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.see.mao.exception.MaoException;
import org.see.mao.persistence.Column;
import org.see.mao.persistence.Id;
import org.see.mao.persistence.JoinColumn;
import org.see.mao.persistence.MetaDataAnnotationConfig;
import org.see.mao.persistence.Table;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.google.common.collect.Lists;

/**
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class AnnotationReflectionHelper {

	/**
	 * Cache for {@link Class and Super #getDeclaredFields()}, allowing for fast iteration.
	 */
	private static final Map<Class<?>, MetaDataAnnotationConfig> annocationFieldsCache = new ConcurrentReferenceHashMap<Class<?>, MetaDataAnnotationConfig>(256);
	
	private static List<Field> findConcreteFields(Class<?> clazz){
		List<Field> fieldList = Lists.newArrayList();
		for(;clazz != Object.class;clazz = clazz.getSuperclass()){
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				fieldList.add(f);
			}
		}
		return fieldList;
	}
	
	public synchronized static MetaDataAnnotationConfig getAnnotationConfig(Class<?> clazz){
		MetaDataAnnotationConfig metaDataAnnotationConfig = annocationFieldsCache.get(clazz);
		if(metaDataAnnotationConfig == null){
			metaDataAnnotationConfig = new MetaDataAnnotationConfig();
			Table table = clazz.getAnnotation(Table.class);
			if(table == null){
				throw new MaoException(clazz+"设置Annocation @Table有误，请确认!");
			}
			boolean version = table.version();
			metaDataAnnotationConfig.setTableName(table.name());
			metaDataAnnotationConfig.setVersion(version);
			List<Field> fieldList = getDeclaredFields(clazz);
			for(Field field : fieldList){
				Column column = field.getAnnotation(Column.class);
				if(column != null){
					metaDataAnnotationConfig.setColumnFields(field);
					continue;
				}
				Id id = field.getAnnotation(Id.class);
				if(id != null){
					metaDataAnnotationConfig.setIdField(field);
					continue;
				}
				JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
				if(joinColumn != null){
					metaDataAnnotationConfig.setJoinColumnFields(field);
					continue;
				}
			}
			annocationFieldsCache.put(clazz, metaDataAnnotationConfig);
		}
		return metaDataAnnotationConfig;
	}
	
	private static List<Field> getDeclaredFields(Class<?> clazz){
		return findConcreteFields(clazz);
	}
	
	public static void clearCache() {
		annocationFieldsCache.clear();
	}
	
}
