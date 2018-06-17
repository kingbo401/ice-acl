package com.kingbo401.iceacl.model.db.param;

import com.kingbo401.commons.model.param.BaseParam;

public class CheckUserPermissionParam extends BaseParam{
	 /**
     * 用户账号
     */
    private String userId;

    /**
     * 权限key
     */
    private String permissionKey;

    /**
     * 租户id
     */
    private String tenant;

    /**
     * 应用id
     */
    private String appKey;

    /**
     * 当授权对象类型为用户时，是否校验关联权限组上的数据
     */
    private boolean hierarchicalCheckPermissionGroup = false;
    /**
     * 授权对象是用户时，是否进行级联查询角色关联的权限组上的数据
     */
    private boolean hierarchicalCheckRolePermissionGroup = false;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPermissionKey() {
		return permissionKey;
	}
	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
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
	public boolean isHierarchicalCheckPermissionGroup() {
		return hierarchicalCheckPermissionGroup;
	}
	public void setHierarchicalCheckPermissionGroup(boolean hierarchicalCheckPermissionGroup) {
		this.hierarchicalCheckPermissionGroup = hierarchicalCheckPermissionGroup;
	}
	public boolean isHierarchicalCheckRolePermissionGroup() {
		return hierarchicalCheckRolePermissionGroup;
	}
	public void setHierarchicalCheckRolePermissionGroup(boolean hierarchicalCheckRolePermissionGroup) {
		this.hierarchicalCheckRolePermissionGroup = hierarchicalCheckRolePermissionGroup;
	}
}
