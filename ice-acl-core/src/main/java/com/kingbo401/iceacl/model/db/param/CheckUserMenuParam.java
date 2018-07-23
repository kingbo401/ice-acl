package com.kingbo401.iceacl.model.db.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

public class CheckUserMenuParam extends BaseParam{
	private String userId;
	private String appKey;
	private String tenant;
	private List<Long> menuIds;
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
	public List<Long> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
}
