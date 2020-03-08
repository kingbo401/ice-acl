package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class UserRoleRefQueryParam extends PageParam{
	private String userId;
	private Integer status;
	private String tenant;
	private String appKey;
	private Long roleId;
    private String roleName;
    private String subgroup;
    private String roleKey;
	/**
             * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = true;

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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}
	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
