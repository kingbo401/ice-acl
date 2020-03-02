package com.kingbo401.acl.common.model.dto.param;

import java.util.Map;

public class DataGrantQueryParam extends BaseDataGrantParam{
	
	private String operationCode;
	
	private boolean hierarchicalRole = false;
	
	private boolean hierarchicalRolePermissionGroup = false;
	
	private boolean hierarchicalPermissionGroup = false;
	
    private boolean returnNotEffective = false;
    
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
