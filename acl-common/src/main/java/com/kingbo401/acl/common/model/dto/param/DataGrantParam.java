package com.kingbo401.acl.common.model.dto.param;

import java.util.List;

import com.kingbo401.acl.common.model.DataGrantRecordInfo;

public class DataGrantParam extends BaseDataGrantParam{
	private List<DataGrantRecordInfo> records;

	public List<DataGrantRecordInfo> getRecords() {
		return records;
	}

	public void setRecords(List<DataGrantRecordInfo> records) {
		this.records = records;
	}
}
