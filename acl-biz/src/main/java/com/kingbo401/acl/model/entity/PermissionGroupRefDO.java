package com.kingbo401.acl.model.entity;

import com.kingbo401.acl.common.model.BaseDO;

public class PermissionGroupRefDO extends BaseDO{
	private Long groupId;
    private Long permissionId;
    private Integer status;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
