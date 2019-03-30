package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class RolePermissionGroupRefPO extends BasePO{
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
