package com.kingbo401.iceacl.model.dto.param;

import com.kingbo401.commons.model.param.PageParam;

public class GrantTargetParam extends PageParam{
	private String appKey;
	
	private String tenant;
	
	private String grantTargetId;
	
	private int grantTargetType;
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getGrantTargetId() {
		return grantTargetId;
	}
	public void setGrantTargetId(String grantTargetId) {
		this.grantTargetId = grantTargetId;
	}
	public int getGrantTargetType() {
		return grantTargetType;
	}
	public void setGrantTargetType(int grantTargetType) {
		this.grantTargetType = grantTargetType;
	}
}
