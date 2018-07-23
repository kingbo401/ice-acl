package com.kingbo401.iceacl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class MenuPermissionRefParam extends BasePojo{
	private String appKey;
	private Long menuId;
	private List<Long> permissionIds;
	private boolean multiApp = false;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public List<Long> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
	public boolean isMultiApp() {
		return multiApp;
	}
	public void setMultiApp(boolean multiApp) {
		this.multiApp = multiApp;
	}
}
