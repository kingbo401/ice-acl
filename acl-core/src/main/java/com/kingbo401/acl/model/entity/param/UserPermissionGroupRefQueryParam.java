package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class UserPermissionGroupRefQueryParam extends PageParam{
	private String appKey;
	private String userId;
	private Integer status;
    private String tenant;
    private Long groupId;
    private String subgroup;
    private String groupName;
    /**
             * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = true;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}
	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
