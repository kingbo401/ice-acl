package com.kingbo401.acl.common.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

public class RolePermissionGroupRefParam extends BaseParam{
	private String appKey;
	private Long roleId;
	private String subgroup;
	private List<Long> groupIds;

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
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public List<Long> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}
}
