package org.see.mao.strategy.uuid;

import java.util.UUID;

/**
 * 通过JDK生成32位随机数（UUID实现）
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class UUIDIdGenerator {
	
	/**
	 * uuid
	 * @return
	 */
	public static String next(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
}
