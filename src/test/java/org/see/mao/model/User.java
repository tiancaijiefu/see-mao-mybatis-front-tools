package org.see.mao.model;

import org.see.mao.persistence.Column;
import org.see.mao.persistence.OneToOne;
import org.see.mao.persistence.Table;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年11月17日
 */
@Table(name = "t_user", version = true)
public class User extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long major;

	@Column(name = "age")
	private int age;

	@Column(name = "name")
	private String name;
	
	@Column(name="org_id")
	private Long orgId;

	@OneToOne(mappedBy="orgId",targetEntity=Org.class)
	private Org org;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the major
	 */
	public Long getMajor() {
		return major;
	}

	/**
	 * @param major
	 *            the major to set
	 */
	public void setMajor(Long major) {
		this.major = major;
	}

	/**
	 * @return the org
	 */
	public Org getOrg() {
		return org;
	}

	/**
	 * @param org
	 *            the org to set
	 */
	public void setOrg(Org org) {
		this.org = org;
	}

	/**
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + getId() + ", age=" + age + ", name=" + name + ", orgId=" + orgId + ", org=" + org + "]";
	}

}
