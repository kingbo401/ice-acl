package com.kingbo401.acl.model.entity.param;

import java.util.List;

import com.kingbo401.commons.model.param.PageParam;

public class UsersRoleRefQueryParam extends PageParam{
    private List<String> userIds;
    private Integer status;
    private String appKey;
    private String tenant;
    private Long roleId;
    private String roleName;
    private String subgroup;
    private String roleKey;
    
    /**
     * false,返回用户拥有的所有角色；true 只返回roleId, roleName,subgroup,roleKey过滤出来的角色
     */
    private boolean filterUserRole = false;
    
    /**
             * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = true;

    public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
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

	public boolean isFilterUserRole() {
		return filterUserRole;
	}

	public void setFilterUserRole(boolean filterUserRole) {
		this.filterUserRole = filterUserRole;
	}

	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}

	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
