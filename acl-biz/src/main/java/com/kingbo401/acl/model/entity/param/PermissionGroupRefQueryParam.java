package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class PermissionGroupRefQueryParam extends PageParam{
	private String appKey;
	private Long groupId;
	private Integer status;
	private String permSubgroup;
	private String permissionKey;
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPermSubgroup() {
		return permSubgroup;
	}

	public void setPermSubgroup(String permSubgroup) {
		this.permSubgroup = permSubgroup;
	}

	public String getPermissionKey() {
		return permissionKey;
	}

	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
	}
}
