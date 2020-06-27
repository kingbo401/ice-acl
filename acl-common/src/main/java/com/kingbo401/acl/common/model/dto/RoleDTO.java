package com.kingbo401.acl.common.model.dto;

import java.util.List;

import com.kingbo401.commons.model.BaseDO;

public class RoleDTO extends BaseDO{
	private String appKey;
	private String roleKey;
	private String name;
	private String nameEn;
	private String description;
	private String tenant;
	private String subgroup;
	private Integer status;
	private String menuSubgroup;
	private List<Long> menuIds;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Long> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
	public String getMenuSubgroup() {
		return menuSubgroup;
	}
	public void setMenuSubgroup(String menuSubgroup) {
		this.menuSubgroup = menuSubgroup;
	}
}
