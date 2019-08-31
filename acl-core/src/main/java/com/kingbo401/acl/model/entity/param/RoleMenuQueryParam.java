package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.OrderParam;

public class RoleMenuQueryParam extends OrderParam{
	private Long roleId;
	private String appKey;
	private Integer status;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
