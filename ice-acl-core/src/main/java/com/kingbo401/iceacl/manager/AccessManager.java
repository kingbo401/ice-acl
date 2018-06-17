package com.kingbo401.iceacl.manager;

import com.kingbo401.iceacl.model.db.param.CheckUserPermissionParam;

public interface AccessManager {
	/**
     * 校验用户是否有
     * @param param
     * @return
     */
    boolean checkUserPermission(CheckUserPermissionParam param);

    /**
     * 校验用户是否有该角色的权限
     * @param userId
     * @param appKey
     * @param roleKey
     * @param tenant
     * @return
     */
    boolean checkUserRole(String userId, String appKey, String roleKey, String tenant);
}
