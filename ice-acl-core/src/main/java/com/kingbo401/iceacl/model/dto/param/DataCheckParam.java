package com.kingbo401.iceacl.model.dto.param;

import java.util.Map;

public class DataCheckParam extends BaseDataGrantParam{

	private String operationCode;
	
	private Map<String, String> data;
	
	private boolean hierarchicalRole = false;
	
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
	public boolean isHierarchicalRole() {
		return hierarchicalRole;
	}
	public void setHierarchicalRole(boolean hierarchicalRole) {
		this.hierarchicalRole = hierarchicalRole;
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
