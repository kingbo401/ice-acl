package com.kingbo401.iceacl.model.po.param;

import com.kingbo401.commons.model.param.PageParam;

public class UserRoleRefQueryParam extends PageParam{
	private String userId;
	private Integer status;
	private String tenant;
	private String appKey;
	private Long roleId;
    private String roleName;//角色名
    private String roleType;//角色类型
    private String roleKey;// 角色key，支持模糊查询
	private String tag1;
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
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}
	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
