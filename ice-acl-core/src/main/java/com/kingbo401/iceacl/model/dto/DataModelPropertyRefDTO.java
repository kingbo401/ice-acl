package com.kingbo401.iceacl.model.dto;

import kingbo401.iceacl.common.model.BasePO;

public class DataModelPropertyRefDTO extends BasePO{
	private Long propertyId;
	private String propertyCode;
	private String propertyName;
	private String propertyDescription;
	private Integer defaultAccessType;
	private Integer status;
	
	public Long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
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
	public Integer getDefaultAccessType() {
		return defaultAccessType;
	}
	public void setDefaultAccessType(Integer defaultAccessType) {
		this.defaultAccessType = defaultAccessType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
