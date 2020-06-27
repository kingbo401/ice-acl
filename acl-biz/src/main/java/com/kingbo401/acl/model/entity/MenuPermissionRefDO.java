package com.kingbo401.acl.model.entity;

import com.kingbo401.commons.model.BaseDO;

public class MenuPermissionRefDO extends BaseDO{
	private Long menuId;
	private Long permissionId;
	private Integer status;
	
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
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
