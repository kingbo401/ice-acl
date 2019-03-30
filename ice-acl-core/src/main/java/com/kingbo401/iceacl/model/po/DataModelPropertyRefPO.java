package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class DataModelPropertyRefPO extends BasePO {
	private Long modelId;
	private Long propertyId;
	private Integer defaultAccessType;
	private Integer status;
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public Long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
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
