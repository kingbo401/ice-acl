package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class RolePermissionRefPO extends BasePO{
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
