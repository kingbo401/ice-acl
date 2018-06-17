package com.kingbo401.iceacl.model.db;

public class DataPropertyAccessDO extends BaseDO{
    private Long modelId;
    private Long propertyId;
    private String grantTargetId;
	private Integer grantTargetType;
    private String tenant;
    private Integer accessType;
    private Integer status;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}
}