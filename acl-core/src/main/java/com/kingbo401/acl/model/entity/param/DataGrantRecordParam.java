package com.kingbo401.acl.model.entity.param;

import com.kingbo401.commons.model.param.PageParam;

public class DataGrantRecordParam extends PageParam{
	private String appKey;
	private Long modelId;
	private String grantTargetId;
	private Integer grantTargetType;
	private String tenant;
	/**
	     * 是否返回没有生效的授权
	*/
	private boolean returnNotEffective = true;
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
	public boolean isReturnNotEffective() {
		return returnNotEffective;
	}
	public void setReturnNotEffective(boolean returnNotEffective) {
		this.returnNotEffective = returnNotEffective;
	}
}
