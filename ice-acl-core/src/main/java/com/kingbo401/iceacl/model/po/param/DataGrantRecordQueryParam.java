package com.kingbo401.iceacl.model.po.param;

import java.util.List;

import com.kingbo401.commons.model.param.PageParam;

public class DataGrantRecordQueryParam extends PageParam{
	private String appKey;
	private Long modelId;
	private Long operationId;
	private List<String> grantTargetIds;
	private Integer grantTargetType;
	private String tenant;
    /**
     * 是否返回没有生效的授权
     */
    private boolean returnNotEffective = false;
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
	public List<String> getGrantTargetIds() {
		return grantTargetIds;
	}
	public void setGrantTargetIds(List<String> grantTargetIds) {
		this.grantTargetIds = grantTargetIds;
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
