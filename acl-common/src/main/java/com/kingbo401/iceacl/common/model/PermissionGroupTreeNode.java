package com.kingbo401.iceacl.common.model;

import java.util.ArrayList;
import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class PermissionGroupTreeNode extends BasePojo{
	private Long id;
	private String appKey;
    private String tenant;
    private String tag1;
    private Long groupPid;
    private String groupType;
    private String groupName;
    private Integer groupOrder;
    private String description;
    private Integer status;
    private Integer level;
	private List<PermissionGroupTreeNode> children = new ArrayList<PermissionGroupTreeNode>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getTag1() {
		return tag1;
	}
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}
	public Long getGroupPid() {
		return groupPid;
	}
	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getGroupOrder() {
		return groupOrder;
	}
	public void setGroupOrder(Integer groupOrder) {
		this.groupOrder = groupOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public List<PermissionGroupTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<PermissionGroupTreeNode> children) {
		this.children = children;
	}
}
