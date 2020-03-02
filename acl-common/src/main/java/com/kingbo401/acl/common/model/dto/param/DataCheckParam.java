package com.kingbo401.acl.common.model.dto.param;

import java.util.Map;

public class DataCheckParam extends BaseDataGrantParam{

	private String operationCode;
	
	private Map<String, String> data;
	
	private boolean hierarchicalCheckRole = false;
	
	private boolean hierarchicalCheckPermissionGroup = false;
	
	private boolean hierarchicalCheckRolePermissionGroup = false;
	
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public boolean isHierarchicalCheckRole() {
		return hierarchicalCheckRole;
	}
	public void setHierarchicalCheckRole(boolean hierarchicalCheckRole) {
		this.hierarchicalCheckRole = hierarchicalCheckRole;
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
