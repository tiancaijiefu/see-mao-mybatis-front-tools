package org.see.mao.dto;

import java.util.List;

/**
 * @author Joshua Wang
 * @date 2016年11月25日
 */
public class DataWrapper {

	private MetaData seeMetaData;

	private List<MetaData> seeMetaDataList;

	/**
	 * @return the seeMetaData
	 */
	public MetaData getSeeMetaData() {
		return seeMetaData;
	}

	/**
	 * @param seeMetaData
	 *            the seeMetaData to set
	 */
	public void setSeeMetaData(MetaData seeMetaData) {
		this.seeMetaData = seeMetaData;
	}

	/**
	 * @return the seeMetaDataList
	 */
	public List<MetaData> getSeeMetaDataList() {
		return seeMetaDataList;
	}

	/**
	 * @param seeMetaDataList
	 *            the seeMetaDataList to set
	 */
	public void setSeeMetaDataList(List<MetaData> seeMetaDataList) {
		this.seeMetaDataList = seeMetaDataList;
	}

}
