package org.see.mao.model;

import java.util.List;

import org.see.mao.persistence.BuiltinAssociation;
import org.see.mao.persistence.Column;
import org.see.mao.persistence.FetchType;
import org.see.mao.persistence.JoinColumn;
import org.see.mao.persistence.OneToMany;
import org.see.mao.persistence.OneToOne;
import org.see.mao.persistence.Table;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年11月17日
 */
@Table(name="t_user",version=true)
@BuiltinAssociation
public class User extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long major;
	
	@Column(name="age")
	private int age;
	
	@Column(name="name")
	private String name;
	
	@JoinColumn(name="org_id",targetEntity=Org.class,fetch=FetchType.LAZY)
	private Org org;
	
	private List<MenuTree> trees;

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
	@OneToOne(mappedBy="org_id",targetEntity=Org.class)
	public Org getOrg() {
		return org;
	}

	/**
	 * @param org the org to set
	 */
	public void setOrg(Org org) {
		this.org = org;
	}

	/**
	 * @return the trees
	 */
	@OneToMany(mappedBy="test_id",targetEntity=MenuTree.class)
	public List<MenuTree> getTrees() {
		return trees;
	}

	/**
	 * @param trees the trees to set
	 */
	public void setTrees(List<MenuTree> trees) {
		this.trees = trees;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [major=" + major + ", name=" + getName() + ", id=" + getId() + "]";
	}

}
