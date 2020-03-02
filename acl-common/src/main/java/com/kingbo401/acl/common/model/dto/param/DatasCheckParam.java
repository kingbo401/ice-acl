package com.kingbo401.acl.common.model.dto.param;

import java.util.List;
import java.util.Map;

public class DatasCheckParam extends BaseDataGrantParam{
	
	private String operationCode;
	
	private List<Map<String, String>> datas;
	
	private boolean hierarchicalCheckRole = false;
	
	private boolean hierarchicalCheckPermissionGroup = false;
	
	private boolean hierarchicalCheckRolePermissionGroup = false;
	
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public List<Map<String, String>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, String>> datas) {
		this.datas = datas;
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
