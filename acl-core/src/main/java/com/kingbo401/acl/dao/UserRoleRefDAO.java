package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.model.entity.UserRoleRefDO;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.model.entity.param.UsersRoleRefQueryParam;

public interface UserRoleRefDAO {
	int batchCreate(@Param("list")List<UserRoleRefDO> userRoleRefs);
	int updateRefsStatus(@Param("userId")Long userId, @Param("tenant") String tenant, @Param("roleIds")List<Long> roleIds, @Param("status")Integer status);
	int removeRefsByRoleId(@Param("roleId")long roleId);
	List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam param);
	List<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam param);
	long countUserRoleRef(UserRoleRefQueryParam param);
	List<Long> pageUser(UsersRoleRefQueryParam param);
	int countUser(UsersRoleRefQueryParam param);
	List<UserRoleRefDTO> listUsersRoleRef(UsersRoleRefQueryParam param);
	UserRoleRefDO hasUserUse(@Param("roleId")long roleId);
	int checkUserRole(@Param("userId")Long userId, @Param("tenant") String tenant, @Param("roleId")long roleId);
	List<String> listUserTenant(@Param("userId")Long userId, @Param("appKey")String appKey, @Param("roleKeys")List<String> roleKeys);
}
