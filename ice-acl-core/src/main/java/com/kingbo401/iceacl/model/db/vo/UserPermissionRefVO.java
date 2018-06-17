package com.kingbo401.iceacl.model.db.vo;

import com.kingbo401.iceacl.model.db.UserPermissionRefDO;

public class UserPermissionRefVO extends UserPermissionRefDO{
	private String userName;
	private String appKey;
	private String permissionTenant;
	private String permissionType;
    private String permissionKey;
    private String permissionName;
    private String description;
	private String tag1;
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPermissionTenant() {
		return permissionTenant;
	}

	public void setPermissionTenant(String permissionTenant) {
		this.permissionTenant = permissionTenant;
	}

	public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
