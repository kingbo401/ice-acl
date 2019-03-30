package com.kingbo401.iceacl.model.po;

import kingbo401.iceacl.common.model.BasePO;

public class PermissionGroupPO extends BasePO {
	private String appKey;
    private String tenant;
    private String tag1;
    private Long groupPid;
    private String groupType;
    private String groupName;
    private Integer groupOrder;
    private String description;
    private Integer status;
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
	public Long getGroupPid() {
		return groupPid;
	}
	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
