package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class PermissonQueryParam extends PageParam{
	private String appKey;
	private String permissionKey;
	private String name;
	private String subgroup;
	private Integer status;
	private boolean onlyTenantGroup;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getPermissionKey() {
		return permissionKey;
	}
	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public boolean isOnlyTenantGroup() {
		return onlyTenantGroup;
	}
	public void setOnlyTenantGroup(boolean onlyTenantGroup) {
		this.onlyTenantGroup = onlyTenantGroup;
	}
}
