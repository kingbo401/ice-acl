package com.kingbo401.acl.common.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DataGrantRecordInfo {
	private Long id;
	private Set<String> operationCodes;
	private Set<PropertyRule> propertyRules;
	private Date effectiveTime;
	private Date expireTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<String> getOperationCodes() {
		return operationCodes;
	}
	public void setOperationCodes(Set<String> operationCodes) {
		this.operationCodes = operationCodes;
	}
	public Set<PropertyRule> getPropertyRules() {
		return propertyRules;
	}
	public void setPropertyRules(Set<PropertyRule> propertyRules) {
		this.propertyRules = propertyRules;
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
	public void addOperationCode(String code) {
		if (operationCodes == null) {
			operationCodes = new HashSet<>();
		}
		operationCodes.add(code);
	}
	public void addPropertyRule(PropertyRule rule) {
		if(propertyRules == null) {
			propertyRules = new HashSet<>();
		}
		propertyRules.add(rule);
	}
}
