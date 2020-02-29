package com.kingbo401.acl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

public class PermissionGroupRefParam extends BaseParam{
	private String appKey;
	private Long groupId;
	private String subgroup;
	private List<Long> permissionIds;

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
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public List<Long> getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
