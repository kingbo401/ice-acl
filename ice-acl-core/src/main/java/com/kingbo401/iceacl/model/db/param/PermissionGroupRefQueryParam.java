package com.kingbo401.iceacl.model.db.param;

import com.kingbo401.commons.model.param.PageParam;

public class PermissionGroupRefQueryParam extends PageParam{
	private String appKey;
	private Long groupId;
	private Integer status;
	private String permissionAppKey;
	private String permissionType;
	private String tag1;
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

	public String getPermissionAppKey() {
		return permissionAppKey;
	}

	public void setPermissionAppKey(String permissionAppKey) {
		this.permissionAppKey = permissionAppKey;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
}
