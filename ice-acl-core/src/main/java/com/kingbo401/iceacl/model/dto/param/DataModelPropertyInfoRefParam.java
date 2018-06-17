package com.kingbo401.iceacl.model.dto.param;

import java.util.List;

import com.kingbo401.commons.model.param.BaseParam;

import kingbo401.iceacl.common.model.PropertyInfo;

public class DataModelPropertyInfoRefParam extends BaseParam{
	private String modelCode;
	private List<PropertyInfo> propertyInfos;
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
	public List<PropertyInfo> getPropertyInfos() {
		return propertyInfos;
	}
	public void setPropertyInfos(List<PropertyInfo> propertyInfos) {
		this.propertyInfos = propertyInfos;
	}
}
