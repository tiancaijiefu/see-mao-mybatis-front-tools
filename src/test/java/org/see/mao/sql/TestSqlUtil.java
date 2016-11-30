package org.see.mao.sql;

import java.text.MessageFormat;
import java.util.List;

import org.see.mao.model.User;

import com.google.common.collect.Lists;

/**
 * @author Joshua Wang
 * @date 2016年11月25日
 */
public class TestSqlUtil {
	
	static List<User> getUsers(){
		
		List<User> list = Lists.newArrayList();
		
		User user = new User();
		user.setId(201L);;
		user.setName("K201");
		user.setAge(21);
		list.add(user);
		
		user = new User();
		user.setId(202L);;
		user.setName("K202");
		user.setAge(22);
		list.add(user);
		
		user = new User();
		user.setId(203L);;
		user.setName("K203");
		user.setAge(23);
		list.add(user);
		
		return list;
	}
	
	public static String getSql(List<User> users,String tableName,String...columns){
		StringBuilder sqlBuilder = new StringBuilder("insert into ").append(tableName).append("(");
		StringBuilder msfBuilder = new StringBuilder("(");
		int len = columns.length;
		for(int i=0;i<len;i++){
			String col = columns[i];
			if(i != 0){
				sqlBuilder.append(",");
				msfBuilder.append(",");
			}
			sqlBuilder.append(col);
			msfBuilder.append("#'{'entities[{0}].").append(col).append("}");
		}
		sqlBuilder.append(") values");
		msfBuilder.append(")");
		System.err.println(msfBuilder.toString());
		MessageFormat msf = new MessageFormat(msfBuilder.toString());
		for(int i=0;i<len;i++){
			sqlBuilder.append(msf.format(new Object[]{i}));
		}
		return sqlBuilder.toString();
	}
	
	public static void main(String[] args) {
		
		List<User> list = getUsers();
		String sql = getSql(list, "t_user", new String[]{"id","name","age"});
		
		System.out.println(sql);
		
	}
	
	
	
}
