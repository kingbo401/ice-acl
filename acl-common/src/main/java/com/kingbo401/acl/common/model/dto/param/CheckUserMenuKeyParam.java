package com.kingbo401.acl.common.model.dto.param;

public class CheckUserMenuKeyParam {
	private String appKey;
	private String userId;
	private String tenant;
	private String menuKey;
	private String subgroup;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getMenuKey() {
		return menuKey;
	}
	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
}
