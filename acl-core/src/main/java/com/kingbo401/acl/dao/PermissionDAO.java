package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.param.PermissonQueryParam;

public interface PermissionDAO {
	int create(PermissionDO permission);

	int batchCreate(@Param("list") List<PermissionDO> permissions);

	int update(PermissionDO permission);

	List<PermissionDO> getPermissionByIds(@Param("ids") List<Long> ids);

	List<PermissionDO> getPermissionByKeys(@Param("appKey") String appKey,
			@Param("permissionKeys") List<String> permissionKeys);

	PermissionDO getById(@Param("id") Long id);
	
	PermissionDO getByKey(@Param("appKey") String appKey, @Param("permissionKey") String permissionKey);

	List<PermissionDO> listPermission(PermissonQueryParam param);

	long countPermission(PermissonQueryParam param);

	List<PermissionDO> pagePermission(PermissonQueryParam param);

	/**
	 * 校验用户是否有该角色下面的权限
	 * 
	 */
	int checkUserRolePermission(@Param("userId") Long userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 校验用户是否直接关联该权限
	 */
	int checkUserDirectPermission(@Param("userId") Long userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 校验用户到权限组,在到权限
	 */
	int checkUserPermissionGroupPermission(@Param("userId") Long userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

	/**
	 * 用户到角色到权限组,到权限
	 */
	int checkUserRolePermissionGroupPermission(@Param("userId") Long userId, @Param("permissionId") Long permissionId,
			@Param("tenant") String tenant);

}
