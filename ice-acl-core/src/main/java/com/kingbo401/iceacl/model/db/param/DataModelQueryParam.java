package com.kingbo401.iceacl.model.db.param;

import com.kingbo401.commons.model.param.PageParam;

public class DataModelQueryParam extends PageParam{
	private String appKey;
	private String code;
	private String name;
	private Integer status;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
