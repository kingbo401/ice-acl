package com.kingbo401.acl.common.model.dto;

import java.util.ArrayList;
import java.util.List;

public class UserRoleRefsDTO {
	private Long userId;
    private String userName;
    private List<UserRoleRefDTO> refs = new ArrayList<UserRoleRefDTO>();
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<UserRoleRefDTO> getRefs() {
		return refs;
	}
	public void setRefs(List<UserRoleRefDTO> refs) {
		this.refs = refs;
	}
}
