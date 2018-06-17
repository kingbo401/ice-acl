package com.kingbo401.iceacl.model.dto.param;

import com.kingbo401.commons.model.param.PageParam;

public class GrantTargetParam extends PageParam{
	private String appKey;
	/**
	 * 租户id，多数情况下是公司ID
	 */
	private String tenant;
	/**
	 * 授权对象id
	 */
	private String grantTargetId;
	/**
	 * 授权对象类型
	 */
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
