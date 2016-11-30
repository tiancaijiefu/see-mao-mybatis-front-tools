package org.see.mao.strategy.snowflake;

import org.see.mao.helpers.StringHelper;
import org.see.mao.strategy.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 		See引擎生成Id工具类
 * </p>
 * @author Joshua Wang
 * @date 2016年7月1日
 */
public class SnowflakeIdGenerator {
	
	private static SnowflakeIdWorker snowflakeIdWorker;
	
	private static Logger logger = LoggerFactory.getLogger(SnowflakeIdGenerator.class);
	
	private static String datacenterId;//数据中心序号
	private static String workerId;//数据中心的机器序号
	private static String twepochStr;//时间戳基数
	
	private SnowflakeIdGenerator(){
		
	}
	
	static{
		workerId = 		PropertiesUtils.getProperty("worker.id");
		datacenterId =  PropertiesUtils.getProperty("datacenter.id");
		twepochStr = 	PropertiesUtils.getProperty("twepoch.long");
		//生成发号器对象
		try {
			snowflakeIdWorker = new SnowflakeIdWorker(Long.parseLong(StringHelper.noNull(workerId)), 
					  Long.parseLong(StringHelper.noNull(datacenterId)),
					  0,
					  Long.parseLong(StringHelper.noNull(twepochStr)));
		} catch (Exception e) {
			logger.error("========Id发号器配置错误,请检查org.see -> application.properties=========", e);
		}
	}
	
	/**
	 * 获取See平台Id
	 * @return
	 */
	public static synchronized Long next() {
		return snowflakeIdWorker.nextId();
	}
	
}
