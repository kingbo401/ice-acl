package com.kingbo401.acl.model.entity;

import kingbo401.iceacl.common.model.BaseDO;

public class RolePermissionGroupRefDO extends BaseDO{
    private Long roleId;
    private Long groupId;
    private Integer status;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
