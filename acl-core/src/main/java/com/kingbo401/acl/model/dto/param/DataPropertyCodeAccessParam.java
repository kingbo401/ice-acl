package com.kingbo401.acl.model.dto.param;

import java.util.List;

public class DataPropertyCodeAccessParam extends BaseDataGrantParam{

	private List<String> propertyCodes;

    private Integer accessType;

	public List<String> getPropertyCodes() {
		return propertyCodes;
	}

	public void setPropertyCodes(List<String> propertyCodes) {
		this.propertyCodes = propertyCodes;
	}

	public Integer getAccessType() {
		return accessType;
	}

	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}
}
