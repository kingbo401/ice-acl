package com.kingbo401.acl.model.entity;

import com.kingbo401.acl.common.model.BaseDO;

public class RolePermissionRefDO extends BaseDO{
	private Long roleId;
	private Long permissionId;
	private Integer status;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
