package com.kingbo401.acl.common.model.dto;

import java.util.Date;
import java.util.List;

import com.kingbo401.acl.common.model.PropertyRule;
import com.kingbo401.commons.model.BaseDO;

public class DataGrantRecordDTO extends BaseDO{
	private String appKey;
	private String modelCode;
	private String grantTargetId;
	private Integer grantTargetType;
	private String tenant;
	private List<DataOperationDTO> operations;
	private List<String> operationCodes;
	private List<PropertyRule> propertyRules;
	private Integer status;
	private Date effectiveTime;
	private Date expireTime;
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getModelCode() {
		return modelCode;
	}
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
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
	public List<DataOperationDTO> getOperations() {
		return operations;
	}
	public void setOperations(List<DataOperationDTO> operations) {
		this.operations = operations;
	}
	public List<String> getOperationCodes() {
		return operationCodes;
	}
	public void setOperationCodes(List<String> operationCodes) {
		this.operationCodes = operationCodes;
	}
	public List<PropertyRule> getPropertyRules() {
		return propertyRules;
	}
	public void setPropertyRules(List<PropertyRule> propertyRules) {
		this.propertyRules = propertyRules;
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
