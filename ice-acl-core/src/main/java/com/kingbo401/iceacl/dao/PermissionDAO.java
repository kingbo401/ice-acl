package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.param.PermissonQueryParam;

public interface PermissionDAO {
	int create(PermissionPO permission);

	int batchCreate(@Param("list") List<PermissionPO> permissions);

	int update(PermissionPO permission);

	List<PermissionPO> getPermissionByIds(@Param("ids") List<Long> ids);

	List<PermissionPO> getPermissionByKeys(@Param("appKey") String appKey,
			@Param("permissionKeys") List<String> permissionKeys);

	PermissionPO getById(@Param("id") Long id);
	
	PermissionPO getByKey(@Param("appKey") String appKey, @Param("permissionKey") String permissionKey);

	List<PermissionPO> listPermission(PermissonQueryParam param);

	long countPermission(PermissonQueryParam param);

	List<PermissionPO> pagePermission(PermissonQueryParam param);

	/**
	 * 校验用户是否有该角色下面的权限
	 * 
	 */
	int checkUserRolePermission(@Param("userId") String userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 校验用户是否直接关联该权限
	 */
	int checkUserDirectPermission(@Param("userId") String userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 校验用户到权限组,在到权限
	 */
	int checkUserPermissionGroupPermission(@Param("userId") String userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 用户到角色到权限组,到权限
	 */
	int checkUserRolePermissionGroupPermission(@Param("userId") String userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

}
