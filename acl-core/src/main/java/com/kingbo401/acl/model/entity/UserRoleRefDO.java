package com.kingbo401.acl.model.entity;

import java.util.Date;

import com.kingbo401.iceacl.common.model.BaseDO;

public class UserRoleRefDO extends BaseDO {
	private Long userId;
	private Long roleId;
	private String tenant;
	private Integer status;
	private Date effectiveTime;
	private Date expireTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
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
}
