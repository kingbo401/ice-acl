package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.model.entity.UserRoleRefDO;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.model.entity.param.UsersRoleRefQueryParam;

public interface UserRoleRefDAO {
	int batchCreate(@Param("list")List<UserRoleRefDO> userRoleRefs);
	int updateRefsStatus(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleIds")List<Long> roleIds, @Param("status")Integer status);
	int removeRefsByRoleId(@Param("roleId")Long roleId);
	List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam param);
	List<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam param);
	long countUserRoleRef(UserRoleRefQueryParam param);
	List<String> pageUser(UsersRoleRefQueryParam param);
	int countUser(UsersRoleRefQueryParam param);
	List<UserRoleRefDTO> listUsersRoleRef(UsersRoleRefQueryParam param);
	UserRoleRefDO hasUserUse(@Param("roleId")Long roleId);
	int checkUserRole(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleId")long roleId);
	List<String> listUserTenant(@Param("userId")String userId, @Param("appKey")String appKey, @Param("roleKeys")List<String> roleKeys);
}
