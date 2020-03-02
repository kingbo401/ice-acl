package com.kingbo401.acl.common.model.dto;

import com.kingbo401.acl.common.model.BaseDO;

public class DataPropertyDTO extends BaseDO{
	private String appKey;
	private Long modelId;
	private String code;
	private String dataType;
	private String config;
	private String name;
	private String nameEn;
	private String description;
	private Integer status;
	private Integer defaultAccessType;

	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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
	public Integer getDefaultAccessType() {
		return defaultAccessType;
	}
	public void setDefaultAccessType(Integer defaultAccessType) {
		this.defaultAccessType = defaultAccessType;
	}
}
