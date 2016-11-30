package org.see.mao.model;

import org.see.mao.persistence.BuiltinAssociation;
import org.see.mao.persistence.OneToOne;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年11月23日
 */
@BuiltinAssociation
public class MenuTree extends BaseModel{

	private static final long serialVersionUID = 1L;

	private String url;

	private String icon;
	
	private Org org;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the org
	 */
	@OneToOne(targetEntity=Org.class,mappedBy="org_id")
	public Org getOrg() {
		return org;
	}

	/**
	 * @param org the org to set
	 */
	public void setOrg(Org org) {
		this.org = org;
	}
	
	

}
