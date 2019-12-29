package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.BaseParam;

public class GetUserMenuParam extends BaseParam{
	private String userId;
	private String appKey;
	private String tenant;
	private String subgroup;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
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
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
}
