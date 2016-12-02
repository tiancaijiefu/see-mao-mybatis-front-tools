package org.see.mao.model;

import java.util.List;

import org.see.mao.persistence.Column;
import org.see.mao.persistence.OneToMany;
import org.see.mao.persistence.Table;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年12月2日
 */
@Table(name="t_role",version=false)
public class Role extends BaseModel{

	private static final long serialVersionUID = 1L;

	@Column(name="role_name")
	private String name;
	
	@OneToMany(
			targetEntity=User.class,
			interTable=true,
			interTableName="t_user_role",
			refColumnName="role_id",
			inverseColumnName="user_id")
	private List<User> users;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
