package org.see.mao.dto;

import java.util.List;

/**
 * @author Joshua Wang
 * @date 2016年11月25日
 */
public class DataWrapper {

	private SeeMetaData seeMetaData;

	private List<SeeMetaData> seeMetaDataList;

	/**
	 * @return the seeMetaData
	 */
	public SeeMetaData getSeeMetaData() {
		return seeMetaData;
	}

	/**
	 * @param seeMetaData
	 *            the seeMetaData to set
	 */
	public void setSeeMetaData(SeeMetaData seeMetaData) {
		this.seeMetaData = seeMetaData;
	}

	/**
	 * @return the seeMetaDataList
	 */
	public List<SeeMetaData> getSeeMetaDataList() {
		return seeMetaDataList;
	}

	/**
	 * @param seeMetaDataList
	 *            the seeMetaDataList to set
	 */
	public void setSeeMetaDataList(List<SeeMetaData> seeMetaDataList) {
		this.seeMetaDataList = seeMetaDataList;
	}

}
