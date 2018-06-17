package com.kingbo401.iceacl.model.db.vo;

import com.kingbo401.iceacl.model.db.UserRoleRefDO;

public class UserRoleRefVO extends UserRoleRefDO{
	private String userName;
	private String appKey;
	private String roleTenant;
	private String roleKey;
	private String roleName;
	private Integer level;
	private String description;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getRoleTenant() {
		return roleTenant;
	}
	public void setRoleTenant(String roleTenant) {
		this.roleTenant = roleTenant;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
