package com.kingbo401.iceacl.model.dto.param;

import java.util.Map;

public class DataGrantQueryParam extends BaseDataGrantParam{
	/**
	 * 数据模型操作编码，可不传值
	 */
	private String operationCode;
	
	/**
	 * 授权对象是用户时，是否进行级联查询角色上的数据;分页查询时无效
	 */
	private boolean hierarchicalRole = false;
	/**
	 * 授权对象是用户时，是否进行级联查询角色关联的权限组上的数据;分页查询时无效
	 */
	private boolean hierarchicalRolePermissionGroup = false;
	/**
	 * 授权对象是用户时，是否进行级联查询权限组上的数据;分页查询时无效
	 */
	private boolean hierarchicalPermissionGroup = false;
	
	/**
     * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = false;
    
	/**
	 * map的key为propertyCode，value为授权的属性值;可只传入部分属性值；可为空
	 */
	private Map<String, String> data;

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public boolean isHierarchicalRole() {
		return hierarchicalRole;
	}

	public void setHierarchicalRole(boolean hierarchicalRole) {
		this.hierarchicalRole = hierarchicalRole;
	}

	public boolean isHierarchicalRolePermissionGroup() {
		return hierarchicalRolePermissionGroup;
	}

	public void setHierarchicalRolePermissionGroup(boolean hierarchicalRolePermissionGroup) {
		this.hierarchicalRolePermissionGroup = hierarchicalRolePermissionGroup;
	}

	public boolean isHierarchicalPermissionGroup() {
		return hierarchicalPermissionGroup;
	}

	public void setHierarchicalPermissionGroup(boolean hierarchicalPermissionGroup) {
		this.hierarchicalPermissionGroup = hierarchicalPermissionGroup;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}

	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
