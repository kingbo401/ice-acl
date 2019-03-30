package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class PermissionGroupRefPO extends BasePO{
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
