package com.kingbo401.acl.common.model.dto.param;

import java.util.Date;
import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class UserPermissionGroupRefParam extends BasePojo{
	private String appKey;
	private String userId;
	private String tenant;
	private String subgroup;
	private List<Long> groupIds;
	private Date effectiveTime;
	private Date expireTime;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public List<Long> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}
	public Date getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
}
