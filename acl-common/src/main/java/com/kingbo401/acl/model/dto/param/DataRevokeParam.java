package com.kingbo401.acl.model.dto.param;

import java.util.List;

public class DataRevokeParam extends BaseDataGrantParam{
	private List<Long> dataGrantRecordIds;
	
	public List<Long> getDataGrantRecordIds() {
		return dataGrantRecordIds;
	}
	public void setDataGrantRecordIds(List<Long> dataGrantRecordIds) {
		this.dataGrantRecordIds = dataGrantRecordIds;
	}
}
