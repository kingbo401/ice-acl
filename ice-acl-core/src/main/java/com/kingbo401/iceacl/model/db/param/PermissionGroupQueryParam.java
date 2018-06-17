package com.kingbo401.iceacl.model.db.param;

import com.kingbo401.commons.model.param.PageParam;

public class PermissionGroupQueryParam extends PageParam{
	private String appKey;
    private String tenant;
    private String groupType;
    private String groupName;
    private Long groupPid;
    private String tag1;
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
	public Long getGroupPid() {
		return groupPid;
	}
	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
}
