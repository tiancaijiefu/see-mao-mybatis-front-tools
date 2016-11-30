package org.see.mao.ref.model;

import org.see.mao.dto.SeeMetaData;
import org.see.mao.helpers.DateHelper;

/**
 * @author Joshua Wang
 * @date 2016年11月17日
 */
public class BaseModel extends SeeMetaData{

	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see org.see.mao.dto.SeeMetaData#iniParamBeforeInsert()
	 */
	@Override
	public void iniParamBeforeInsert() {
		setCreateUser(1L);
		setUpdateUser(1L);
		String time = DateHelper.getDate("yyyy-MM-dd HH:mm:ss.SSS");
		setCreateTime(time);
		setUpdateTime(time);
	}

	/* (non-Javadoc)
	 * @see org.see.mao.dto.SeeMetaData#iniParamBeforeUpdate()
	 */
	@Override
	public void iniParamBeforeUpdate() {
		String time = DateHelper.getDate("yyyy-MM-dd HH:mm:ss.SSS");
		setVersion("2016-11-29 09:34:02.311");
		setUpdateTime(time);
		setUpdateUser(1L);
	}

}
