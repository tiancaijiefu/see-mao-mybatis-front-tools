package org.see.mao.strategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Joshua Wang
 * @date 2016年7月1日
 */
public class PropertiesUtils{
	
	final static String APPLICATION_CONFIG = "application.properties";
	/**
	 * 全局配置承载对象
	 */
	final static Properties APPLICATION_PROPS = new Properties();
	
	static{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(APPLICATION_CONFIG);
		try {
			APPLICATION_PROPS.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return APPLICATION_PROPS.getProperty(key);
	}
	
}
