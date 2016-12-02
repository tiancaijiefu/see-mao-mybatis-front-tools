package org.see.mao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.see.mao.dto.SeeMetaData;
import org.see.mao.dto.SeePaginationList;
import org.see.mao.dto.datatables.PagingCriteria;
import org.see.mao.helpers.ConvertHelper;
import org.see.mao.helpers.proxy.MaoProxy;
import org.see.mao.helpers.reflex.Reflections;
import org.see.mao.mapper.BaseMapper;
import org.see.mao.mapper.OrgMapper;
import org.see.mao.mapper.RoleMapper;
import org.see.mao.mapper.UserMapper;
import org.see.mao.model.Org;
import org.see.mao.model.Role;
import org.see.mao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)//使用junit4进行测试
@ContextConfiguration("classpath*:spring-config.xml")//加载配置文件
@Transactional
public class AppTest extends TestCase {
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	OrgMapper orgMapper;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Autowired
	UserService userService;
	
//	@Autowired
//	private BaseMapper<SeeMetaData> baseMapper;
	
	@Test
	public void testConvert(){
		String sql = "select t.id,t.name,t.age major from t_user t";
		sql = "select t.id,t.name,t.age major from t_user t where id=#{id}";
		
//		List<User> userLst = ConvertHelper.convert(menuMapper.getMapList(null, sql), User.class);
		
		User user = ConvertHelper.convert(userMapper.getMap(6, sql), User.class);
		
		System.out.println(user);
	}
	
	@Test
	public void testServiceMehotd(){
		
		User user = new User();
		String sql = "select id,name,age major from t_user where id=#{userid}";
		
		//userMapper.getList(user, sql);
		user = userMapper.getOne(5L, sql);
		System.out.println(user);
	}
	
	@Test
	public void pageList(){
		
//		MenuTree menuTree = new MenuTree();
//		
//		PagingCriteria pc = menuTree.getPagingCriteria();
//		
//		menuTree.getSeeListBean().setPageNo(3);
//		
//		
//		String sql = "select id,menu_name name,menu_url url,icon_skin icon from wp_menu";
//		
//		SeePaginationList<MenuTree> list = menuMapper.listByPage(menuTree.getPagingCriteria(), menuTree, sql);
//		
//		System.err.println(list.get(0).getOrg());
//		System.err.println(list.get(1).getOrg());
		
	}
	
	@Test
	public void testBeanUtil(){
		
		User user = new User();
		
		Map<String,Object> map = Maps.newHashMap();
		map.put("id", 5L);
		map.put("name", "Joshua");
		map.put("major", 0);
		
		try {
			BeanUtils.populate(user, map);
			System.out.println(user);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Rollback(false)
	public void testSave(){
//		User user = new User();
//		user.setId(1012L);
//		user.setName("NOVersion_jAKK");
//		user.setAge(33);
//		user.iniParamBeforeInsert();
//		String sql = "insert into t_user(id,name,age) values(#{id},#{name},#{age})";
//		userMapper.saveEntity(user, sql);
		
		Org org = new Org();
		org.setId(2L);
		org.setOrgName("测试部门");
		
		orgMapper.save(org);
		
	}
	
	@Test
	@Rollback(false)
	public void testAutoSave(){
		User user = new User();
		user.setId(20315L);
		user.setName("TestOrg");
		user.setAge(24);
		user.setOrgId(1L);
		user.iniParamBeforeInsert();
		userMapper.save(user);
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
	@Rollback(false)
	public void testSaveBatch(){
		List<User> users = getUsers();
		
		int count = userMapper.saveEntities(users, "t_user", new String[]{"id","name","age"}, new String[]{"id","name","age"});
		
		System.err.println("操作的记录数为【"+count+"】条");
	}
	
	@Test
	@Rollback(false)
	public void testDelete(){
		
		userMapper.delete(User.class, 6L);
		
	}
	
	@Test
	public void helloMyBatis(){
		User user = new User();
//		user.setId(1L);
//		user.setName("Joshua");
//		user.setMajor(20L);
		
//		System.out.println("=========="+userMapper.get(1L));
//		System.out.println("=========="+userMapper.findList());
		String sql = "select id,name,age from t_user";
//		
//		System.err.println(userMapper);
//		String sql = "insert into t_user values(5,'Kate',21),(6,'Lyli',21)";
//		userMapper.save(user, sql);
		
		
//		userMapper.insert();
		
		sql = "select t.id,t.name,t.age major from t_user t";
		List<User> list = null;//userMapper.listByPage(user.getPagingCriteria(), user, sql);
//		list = userMapper.getList(user, sql);
		
//		List<Map<String,String>> mapList = userMapper.getMapList(null, sql);
//		list = ConvertHelper.convert(mapList, User.class);
//		user = ConvertHelper.convert(userMapper.getMap(5, "select t.id,t.name,t.age major from t_user t where id=#{id}"), User.class);
//		Object userObj = userMapper.getMapsByParam("K202", "select t.id,t.name,t.age major from t_user t where name=#{name}");
		
		user = (User) userMapper.get(User.class, 20313);
		
		System.out.println(user);
	}
	
	
	@Test
	public void testAutoGetOneToOne(){
		
		User user = userMapper.get(User.class, 20313);
		
		System.out.println(user.getOrg().getOrgName());
		
	}
	
	
	@Test
	public void testAutoGetOneToMany1(){
		
		Org org  = orgMapper.get(Org.class, 1L);
		
		System.out.println(org.getUsers());
		
	}
	
	@Test
	public void testAutoGetOneToMany2(){
		
		Role role = roleMapper.get(Role.class, 1L);
		
		System.out.println(role.getUsers().get(0).getOrg().getOrgName());
		
	}
	
	@Test
	@Rollback(false)
	public void testAutoUpdate(){
		User user = userMapper.get(User.class, 20313L);
		user.setName("测试姓名2");
		int count = userMapper.update(user);
		System.err.println("count : ["+count+"]");
	}
	
	@Test
	@Rollback(false)
	public void testUpdate(){
		String sql = "update t_user set name=#{name} where id=#{id}";
		
		//sql = "insert into t_user values(#{id},#{name},#{age})";
		
//		User user = new User();
//		user.setId(20313L);
//		user.setName("yyAutoUpdate");
//		user.setAge(24);
//		user.iniParamBeforeUpdate();
		
//		int count = userMapper.updateEntity(user, sql);
		
		User user = userMapper.get(User.class, 20313L);
		
		user.setName("测试姓名");
		
		int count = userMapper.update(user);
		
		
		System.err.println("count : ["+count+"]");
	}
	
	@Test
	public void testProxy(){
		
		User user = new User();
		user.setId(1L);
		user.setName("Joshua");
		
		user = new MaoProxy().getProxy(user);
		
		System.out.println(Reflections.getUserClass(user.getClass()));
		
	}
	
}
