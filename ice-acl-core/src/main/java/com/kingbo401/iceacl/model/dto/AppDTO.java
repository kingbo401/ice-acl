package com.kingbo401.iceacl.model.dto;

import com.kingbo401.iceacl.model.db.BaseDO;

public class AppDTO extends BaseDO{
	private String appName;
	private String appKey;
	private String description;
	private Integer status;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
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
