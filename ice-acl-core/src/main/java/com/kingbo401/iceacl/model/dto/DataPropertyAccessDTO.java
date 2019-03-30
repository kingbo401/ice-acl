package com.kingbo401.iceacl.model.dto;

import kingbo401.iceacl.common.model.BasePO;

public class DataPropertyAccessDTO extends BasePO{
	private String propertyCode;
	private String propertyName;
	private String propertyDescription;
	private Integer accessType;
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyDescription() {
		return propertyDescription;
	}
	public void setPropertyDescription(String propertyDescription) {
		this.propertyDescription = propertyDescription;
	}
	public Integer getAccessType() {
		return accessType;
	}
	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}
}
