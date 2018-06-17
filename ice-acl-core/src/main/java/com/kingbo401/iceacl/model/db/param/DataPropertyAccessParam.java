package com.kingbo401.iceacl.model.db.param;

import java.util.List;

import com.kingbo401.commons.model.param.OrderParam;

public class DataPropertyAccessParam extends OrderParam{
    private Long modelId;

    private String grantTargetId;
    
	private Integer grantTargetType;

    private String tenant;

    private Integer status;
    
    private Integer accessControl;

    private List<Long> propertyIds;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

	public Integer getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(Integer accessControl) {
		this.accessControl = accessControl;
	}

	public List<Long> getPropertyIds() {
        return propertyIds;
    }

    public void setPropertyIds(List<Long> propertyIds) {
        this.propertyIds = propertyIds;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}    
}
