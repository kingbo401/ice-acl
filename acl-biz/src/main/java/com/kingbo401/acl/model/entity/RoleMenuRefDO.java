package com.kingbo401.acl.model.entity;

import com.kingbo401.commons.model.BaseDO;

public class RoleMenuRefDO extends BaseDO{
	private Long roleId;
    private Long menuId;
    private Integer status;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
