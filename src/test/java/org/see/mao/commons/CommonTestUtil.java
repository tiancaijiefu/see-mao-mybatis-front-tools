package org.see.mao.commons;
import java.util.List;

import org.junit.Test;
import org.see.mao.dto.SeeMetaData;
import org.see.mao.helpers.proxy.MaoProxy;
import org.see.mao.helpers.sql.MaoSQLBuilderHelper;
import org.see.mao.model.User;

import com.google.common.collect.Lists;


/**
 * @author Joshua Wang
 * @date 2016年11月28日
 */
public class CommonTestUtil {
	
	@Test
	public void testBatch(){
		
		
	}

	@Test
	public void testFormatSQL(){
		
		String sql = "select * from t_user where id=#{    id} and name=#{ name }";
		
		sql = sql.replaceAll("#\\{.*?\\}", "#{id}");
		
		System.out.println(sql);
		
	}
	
	@Test
	public void createSql(){
		
		Class<?> clazz = User.class;
		
//		String sql = "insert into t_user(id,name,age) values(#{id},#{name},#{age})";
//		
//		String formatSql = MaoSQLBuilderHelper.formatCustomInsertSql(clazz, sql);
//		String sql = MaoSQLBuilderHelper.builderAutoInsertSql(clazz);
//		sql = MaoSQLBuilderHelper.builderAutoQuerySql(clazz);
		
		User user = new User();
		user.setId(10L);
		
		String sql = MaoSQLBuilderHelper.builderAutoUpdateSql(user);
		
		System.out.println("sql->"+sql);
	}
	

	@Test
	public void testProxyCopyProperties(){
		User user = new User();
		user.setId(5L);
		user.setName("Kate");
		user.setAge(25);
		
		user = new MaoProxy().getProxy(user);
		System.out.println(user.getName());
		System.out.println(user.getOrg());
	}

	@Test
	public void testProxy(){
		User user1 = new User();
		user1.setName("Joshua1");
		User user2 = new User();
		user2.setName("Joshua2");
		
		System.out.println(user1);
		System.out.println(user2);
		
		List<User> userList = Lists.newArrayList();
		userList.add(user1);
		userList.add(user2);
		
		
		MaoProxy maoProxy = new MaoProxy();
		System.err.println("user1================================"+user1);
		long start = System.currentTimeMillis();
		user1 = maoProxy.getProxy(user1);
//		maoProxy = new MaoProxy();
//		user2 = maoProxy.getProxy(user2,user2.getClass());
		long end = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>"+(end - start));
		
		System.err.println("name/////////////////"+user1.getName());
		System.err.println("org/////////////////"+user1.getOrg());
//		System.out.println(user2);
		
	}
	
	
	static List<User> getUsers(){
		
		List<User> list = Lists.newArrayList();
		
		User user = new User();
		user.setId(20111L);;
		user.setName("NV_YK201");
		user.setAge(21);
		user.iniParamBeforeInsert();
		list.add(user);
		
		user = new User();
		user.setId(20212L);;
		user.setName("NV_YK202");
		user.setAge(22);
		user.iniParamBeforeInsert();
		list.add(user);
		
		user = new User();
		user.setId(20313L);;
		user.setName("NV_YK203");
		user.setAge(23);
		user.iniParamBeforeInsert();
		list.add(user);
		
		return list;
	}
	
	@Test
	public void testTime(){
		List<User> users = getUsers();
		long start = System.currentTimeMillis();
		users = new MaoProxy().getProxy(users);
		long end = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>"+(end - start));
		
	}
	
	
	
	@Test
	public void testInstance(){
		User user = new User();
		boolean flag = SeeMetaData.class.isAssignableFrom(user.getClass());
		System.out.println(flag);
	}
	
	
	
	
	
	
	
	
	
	
}
