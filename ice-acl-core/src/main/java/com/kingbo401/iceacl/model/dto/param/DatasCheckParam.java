package com.kingbo401.iceacl.model.dto.param;

import java.util.List;
import java.util.Map;

public class DatasCheckParam extends BaseDataGrantParam{
	/**
	 * 数据模型操作编码
	 */
	private String operationCode;
	/**
	 * map的key为propertyCode，value为授权的属性值列表;map的keyset值及size必须入相应模型属性列表一致
	 */
	private List<Map<String, String>> datas;
	
	/**
	 * 授权对象是用户时，是否进行级联查询角色上的数据、
	 */
	private boolean hierarchicalRole = false;
	
	/**
	 * 当授权对象类型为用户时，是否校关联验权限组
	 */
	private boolean hierarchicalCheckPermissionGroup = false;
	/**
	 * 授权对象是用户时，是否进行级联校验角色关联的权限组上的数据
	 */
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
