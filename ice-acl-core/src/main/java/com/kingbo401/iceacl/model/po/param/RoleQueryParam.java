package com.kingbo401.iceacl.model.po.param;

import com.kingbo401.commons.model.param.PageParam;

public class RoleQueryParam extends PageParam{
	private String appKey;
	private String tenant;
	private String tag1;
	private Long roleId;
	private String roleKey;
	private String roleName;
	private String roleType;
	private Integer status;
	private Integer levelEq;//level等于
	private Integer levelGe;//level大于等于
	private Integer levelLe;//level小于等于
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public Integer getLevelEq() {
		return levelEq;
	}
	public void setLevelEq(Integer levelEq) {
		this.levelEq = levelEq;
	}
	public Integer getLevelGe() {
		return levelGe;
	}
	public void setLevelGe(Integer levelGe) {
		this.levelGe = levelGe;
	}
	public Integer getLevelLe() {
		return levelLe;
	}
	public void setLevelLe(Integer levelLe) {
		this.levelLe = levelLe;
	}
}
