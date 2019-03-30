package com.kingbo401.iceacl.model.dto;

import kingbo401.iceacl.common.model.BasePO;

public class DataOperationDTO extends BasePO{
	private Long modelId;
	private String code;
	private String name;
	private String description;
	private Integer status;
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
