package org.see.mao.model;

import org.see.mao.persistence.Column;
import org.see.mao.persistence.Table;
import org.see.mao.ref.model.BaseModel;

/**
 * @author Joshua Wang
 * @date 2016年12月5日
 */
@Table(name="wp_menu",version=true)
public class MenuTree extends BaseModel{

	private static final long serialVersionUID = 1L;

	@Column(name="menu_name")
	private String menuName;
	
	@Column(name="menu_url")
	private String url;
	
	@Column(name="icon_skin")
	private String icon;

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
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
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
