package com.kingbo401.iceacl.model.db;

public class PermissionGroupLite {
	private Long id;
    private Long groupPid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGroupPid() {
		return groupPid;
	}
	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
	}
}
