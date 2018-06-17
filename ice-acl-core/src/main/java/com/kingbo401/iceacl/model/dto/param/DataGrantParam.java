package com.kingbo401.iceacl.model.dto.param;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kingbo401.iceacl.common.model.PropertyValue;

public class DataGrantParam extends BaseDataGrantParam{
	/**
	 * 数据模型操作编码
	 */
	private String operationCode;
	
	private List<Map<String, PropertyValue>> datas;
	
	/**
	 * 生效时间
	 */
	private Date effectiveTime;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public List<Map<String, PropertyValue>> getDatas() {
		return datas;
	}
	public void setDatas(List<Map<String, PropertyValue>> datas) {
		this.datas = datas;
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
