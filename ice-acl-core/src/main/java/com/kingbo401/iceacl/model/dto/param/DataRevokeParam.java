package com.kingbo401.iceacl.model.dto.param;

import java.util.List;

public class DataRevokeParam extends BaseDataGrantParam{

	/**
	 * 数据模型操作编码，dataGrantRecordId不为null时，operationCode不能为空
	 */
	private String operationCode;
	
	/**
	 * 授权记录id，可为空，不为空则直接回收dataGrantRecordId对应的数据
	 */
	private List<Long> dataGrantRecordIds;
	
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public List<Long> getDataGrantRecordIds() {
		return dataGrantRecordIds;
	}
	public void setDataGrantRecordIds(List<Long> dataGrantRecordIds) {
		this.dataGrantRecordIds = dataGrantRecordIds;
	}
}
