package com.kingbo401.iceacl.common.model;

import java.util.ArrayList;
import java.util.List;

import com.kingbo401.commons.model.BasePojo;

public class MenuTreeNode extends BasePojo{
	private Long id;
	private String name;
	private String enName;
	private Long pid;
	private String url;
	private String icon;
	private Integer type;
	private Integer idx;
	private Integer level;
	private List<MenuTreeNode> children = new ArrayList<MenuTreeNode>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public List<MenuTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeNode> children) {
		this.children = children;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
}
