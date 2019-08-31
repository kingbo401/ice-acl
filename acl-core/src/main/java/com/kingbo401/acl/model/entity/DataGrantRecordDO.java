package com.kingbo401.acl.model.entity;

import java.util.Date;

import kingbo401.iceacl.common.model.BaseDO;

public class DataGrantRecordDO extends BaseDO{
	private String appKey;
	private Long modelId;
	private Long operationId;
	private String grantTargetId;
	private Integer grantTargetType;
	private String tenant;
	private Integer status;
	private Date effectiveTime;
	private Date expireTime;
	
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public Long getOperationId() {
		return operationId;
	}
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}
	public String getGrantTargetId() {
		return grantTargetId;
	}
	public void setGrantTargetId(String grantTargetId) {
		this.grantTargetId = grantTargetId;
	}
	public Integer getGrantTargetType() {
		return grantTargetType;
	}
	public void setGrantTargetType(Integer grantTargetType) {
		this.grantTargetType = grantTargetType;
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
