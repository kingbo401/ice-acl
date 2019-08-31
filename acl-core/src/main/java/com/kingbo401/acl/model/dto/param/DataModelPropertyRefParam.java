package com.kingbo401.acl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

public class DataModelPropertyRefParam extends BaseParam{
	private String modelCode;
	private List<String> propertyCodes;
	private String appKey;
	
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
	public List<String> getPropertyCodes() {
		return propertyCodes;
	}
	public void setPropertyCodes(List<String> propertyCodes) {
		this.propertyCodes = propertyCodes;
	}
}
