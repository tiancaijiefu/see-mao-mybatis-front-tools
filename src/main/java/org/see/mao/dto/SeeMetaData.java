package org.see.mao.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.see.mao.dto.datatables.PagingCriteria;
import org.see.mao.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Joshua Wang
 * @date 2016年7月1日
 */
public abstract class SeeMetaData implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	private Long id;

	/**
	 * 创建者ID
	 */
	private Long createUser;

	/**
	 * 最后修改者ID
	 */
	private Long updateUser;

	/**
	 * 创建时间 parttern yyyy-MM-dd HH:mm:ss.SSS
	 */
	private String createTime;

	/**
	 * 当前对象版本version，用以实现乐观锁
	 * version为记录上次最后修改时间
	 * 最后修改时间 parttern yyyy-MM-dd HH:mm:ss.SSS
	 */
	private String updateTime;
	
	/**
	 * 当前对象版本version，用以实现乐观锁
	 */
	private String version;
	
	/**
	 * See平台List包装器
	 */
	private SeePaginationList<SeeMetaData> seeListBean;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createUser
	 */
	public Long getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the updateUser
	 */
	public Long getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	@Pattern(regexp = "\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}", message = "{最新修改日期非法，请确认!}")
	@NotEmpty(message = "请正确设置最新修改时间")
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 移除版本控制
	 * 		将修改时间、修改人设置为null
	 */
	public void removeVersion(){
		setUpdateTime(null);
		setUpdateUser(null);
	}

	/**
	 * @return the seeListBean
	 */
	@JsonIgnore
	public SeePaginationList<SeeMetaData> getSeeListBean() {
		return seeListBean;
	}

	/**
	 * @param seeListBean
	 *            the seeListBean to set
	 */
	public void setSeeListBean(SeePaginationList<SeeMetaData> seeListBean) {
		this.seeListBean = seeListBean;
	}

	/**
	 * 获取分页参数
	 * 
	 * @return
	 */
	@JsonIgnore
	public PagingCriteria getPagingCriteria() {
		if (seeListBean == null) {
			seeListBean = new SeePaginationList<SeeMetaData>();
		}
		return seeListBean.getPagingCriteria();
	}

	/**
	 * 插入之前执行方法
	 */
	public abstract void iniParamBeforeInsert();

	/**
	 * 更新之前执行方法
	 */
	public abstract void iniParamBeforeUpdate();

}
