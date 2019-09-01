package com.kingbo401.iceacl.common.model;

import com.kingbo401.commons.model.BasePojo;

public class PropertyValue extends BasePojo{
	/**
	 * 属性值
	 */
	private String propertyValue;
	/**
	 * 属性值描述
	 */
	private String propertyValueDesc;
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyValueDesc() {
		return propertyValueDesc;
	}
	public void setPropertyValueDesc(String propertyValueDesc) {
		this.propertyValueDesc = propertyValueDesc;
	}
}
