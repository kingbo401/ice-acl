package com.kingbo401.iceacl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

public class RoleMenuIdRefParam extends BaseParam{
	private String appKey;
	private Long roleId;
	private List<Long> menuIds;
	private boolean multiApp = false;
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public List<Long> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
	public boolean isMultiApp() {
		return multiApp;
	}
	public void setMultiApp(boolean multiApp) {
		this.multiApp = multiApp;
	}
}
