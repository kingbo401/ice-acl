package com.kingbo401.acl.model.dto.param;

import java.util.List;

public class DataRevokeParam extends BaseDataGrantParam{

	private String operationCode;
	
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
