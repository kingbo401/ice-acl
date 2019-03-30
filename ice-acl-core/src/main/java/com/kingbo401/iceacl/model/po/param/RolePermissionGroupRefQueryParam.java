package com.kingbo401.iceacl.model.po.param;

import com.kingbo401.commons.model.param.PageParam;

public class RolePermissionGroupRefQueryParam extends PageParam{
	private String appKey;
	private Long roleId;
	private Long groupId;
	private String groupAppKey;
	private String tenant;
    private String groupType;
    private String groupName;
    private String tag1;
    private Integer status;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getGroupAppKey() {
		return groupAppKey;
	}
	public void setGroupAppKey(String groupAppKey) {
		this.groupAppKey = groupAppKey;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
