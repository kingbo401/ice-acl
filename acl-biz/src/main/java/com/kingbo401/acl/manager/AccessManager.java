package com.kingbo401.acl.manager;

import com.kingbo401.acl.common.model.dto.param.CheckUserMenuKeyParam;
import com.kingbo401.acl.common.model.dto.param.CheckUserMenuUrlParam;
import com.kingbo401.acl.model.entity.param.CheckUserPermissionParam;

public interface AccessManager {
	/**
	   * 校验用户是否有
	 * 
	 * @param param
	 * @return
	 */
	boolean checkUserPermission(CheckUserPermissionParam param);

	/**
	   * 校验用户是否有该角色的权限
	 * 
	 * @param userId
	 * @param tenant
	 * @param appKey
	 * @param roleKey
	 * @return
	 */
	boolean checkUserRole(String userId, String tenant, String appKey, String roleKey);
	
	/**
	 * 根据菜单key判断用户是否有菜单权限
	 * @param param
	 * @return
	 */
	boolean checkUserMenuKey(CheckUserMenuKeyParam param);
	
	/**
	 * 根据菜单url判断用户是否有菜单权限
	 * @param param
	 * @return
	 */
	boolean checkUserMenuUrl(CheckUserMenuUrlParam param);

}
