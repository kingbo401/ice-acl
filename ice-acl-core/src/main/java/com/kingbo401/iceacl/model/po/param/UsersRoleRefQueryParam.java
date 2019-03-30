package com.kingbo401.iceacl.model.po.param;

import java.util.List;

import com.kingbo401.commons.model.param.PageParam;

public class UsersRoleRefQueryParam extends PageParam{
    private List<String> userIds;
    private Integer status;
    private String appKey;
    private String tenant;
    private String tag1;
    private Long roleId;
    private String roleName;//角色名
    private String roleType;//角色类型
    private String roleKey;// 角色key，支持模糊查询
    
    /**
     * false,返回用户拥有的所有角色；true 只返回roleId, roleName,roleType,roleKey过滤出来的角色
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

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
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
