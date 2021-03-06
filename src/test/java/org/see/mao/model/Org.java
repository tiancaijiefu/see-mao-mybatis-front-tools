package org.see.mao.model;

import java.util.List;

import org.see.mao.persistence.Column;
import org.see.mao.persistence.OneToMany;
import org.see.mao.persistence.Table;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年11月29日
 */
@Table(name="t_org",version=false)
public class Org extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	@Column(name="org_name")
	private String orgName;
	
	@OneToMany(targetEntity=User.class,associateColumnName="org_id")
	private List<User> users;

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
