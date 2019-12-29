package com.kingbo401.acl.model.dto;

import java.util.Date;

public class UserPermissionGroupRefDTO {
	private Long id;
	private Date createTime;
	private Date updateTime;
	private Long userId;
	private Long groupId;
	private String tenant;
	private int status;
	private Date effectiveTime;
	private Date expireTime;
	private String userName;
	private String appKey;
	private String groupTenant;
    private Long groupPid;
    private String groupName;
    private String groupDescription;
    private Integer groupIdx;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getGroupTenant() {
		return groupTenant;
	}

	public void setGroupTenant(String groupTenant) {
		this.groupTenant = groupTenant;
	}

	public Long getGroupPid() {
		return groupPid;
	}

	public void setGroupPid(Long groupPid) {
		this.groupPid = groupPid;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupIdx() {
		return groupIdx;
	}

	public void setGroupIdx(Integer groupIdx) {
		this.groupIdx = groupIdx;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
}
