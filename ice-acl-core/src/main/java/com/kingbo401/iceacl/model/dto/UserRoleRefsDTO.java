package com.kingbo401.iceacl.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;

public class UserRoleRefsDTO {
	private String userId;
    private String userName;
    private List<UserRoleRefVO> refVOs = new ArrayList<UserRoleRefVO>();
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<UserRoleRefVO> getRefVOs() {
		return refVOs;
	}
	public void setRefVOs(List<UserRoleRefVO> refVOs) {
		this.refVOs = refVOs;
	}
}
