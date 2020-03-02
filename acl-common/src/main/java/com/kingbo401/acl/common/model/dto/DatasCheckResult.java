package com.kingbo401.acl.common.model.dto;

import java.util.List;

public class DatasCheckResult{
	private boolean allSuccess;//是否每个都验证通过
	private List<Boolean> resultDetails;//每条记录的验证结果
	
	public List<Boolean> getResultDetails() {
		return resultDetails;
	}
	public void setResultDetails(List<Boolean> resultDetails) {
		this.resultDetails = resultDetails;
	}
	public boolean isAllSuccess() {
		return allSuccess;
	}
	public void setAllSuccess(boolean allSuccess) {
		this.allSuccess = allSuccess;
	}
}
