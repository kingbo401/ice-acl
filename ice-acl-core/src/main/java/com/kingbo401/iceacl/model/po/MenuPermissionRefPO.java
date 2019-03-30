package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class MenuPermissionRefPO extends BasePO{
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
