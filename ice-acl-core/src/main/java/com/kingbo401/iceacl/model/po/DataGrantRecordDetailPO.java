package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class DataGrantRecordDetailPO extends BasePO{
	private Long dataGrantRecordId;
	private Long propertyId;
	private String propertyValue;
	private String propertyValueDesc;
	
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getPropertyValueDesc() {
		return propertyValueDesc;
	}

	public void setPropertyValueDesc(String propertyValueDesc) {
		this.propertyValueDesc = propertyValueDesc;
	}
	
	public Long getDataGrantRecordId() {
		return dataGrantRecordId;
	}

	public void setDataGrantRecordId(Long dataGrantRecordId) {
		this.dataGrantRecordId = dataGrantRecordId;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
