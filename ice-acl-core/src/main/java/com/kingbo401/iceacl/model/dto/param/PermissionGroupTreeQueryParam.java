package com.kingbo401.iceacl.model.dto.param;

import com.kingbo401.commons.model.BasePojo;

public class PermissionGroupTreeQueryParam extends BasePojo{
	private String appKey;
    private String tenant;
    private String groupType;
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
}