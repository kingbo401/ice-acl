package com.kingbo401.iceacl.model.db.vo;

import com.kingbo401.iceacl.model.db.UserPermissionGroupRefDO;

public class UserPermissionGroupRefVO extends UserPermissionGroupRefDO{
	private String userName;
	private String appKey;
	private String groupType;
	private String groupTenant;
    private Long groupPid;
    private String groupName;
    private String description;
    private String tag1;
    private Integer groupOrder;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupTenant() {
		return groupTenant;
	}

	public void setGroupTenant(String groupTenant) {
		this.groupTenant = groupTenant;
	}

	public Long getGroupPid() {
		return groupPid;
	}

	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(Integer groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
}
