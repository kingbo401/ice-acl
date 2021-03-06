package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class RolePermissionRefQueryParam extends PageParam{
	private Long roleId;
	private String appKey;
	private String subgroup;
	private String permissionKey;
	private Integer status;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}

	public String getPermissionKey() {
		return permissionKey;
	}

	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
