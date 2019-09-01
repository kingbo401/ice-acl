package com.kingbo401.iceacl.common.model;

import java.util.ArrayList;
import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class PermissionGroupTreeNode extends BasePojo{
	private Long id;
	private String appKey;
    private String tenant;
    private Long pid;
    private String type;
    private String name;
    private String enName;
    private Integer idx;
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
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
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
