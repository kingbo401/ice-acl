package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class RoleMenuRefPO extends BasePO{
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
