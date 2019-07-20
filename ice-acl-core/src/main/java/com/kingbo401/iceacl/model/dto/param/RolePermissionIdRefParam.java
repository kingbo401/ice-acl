package com.kingbo401.iceacl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class RolePermissionIdRefParam extends BasePojo{
	private String appKey;
	private Long roleId;
	private List<Long> permissionIds;
	
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
	public List<Long> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
