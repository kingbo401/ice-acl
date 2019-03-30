package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.dto.UserRoleRefDTO;
import com.kingbo401.iceacl.model.po.UserRoleRefPO;
import com.kingbo401.iceacl.model.po.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.po.param.UsersRoleRefQueryParam;

public interface UserRoleRefDAO {
	int batchCreate(@Param("list")List<UserRoleRefPO> userRoleRefs);
	int updateRefsStatus(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleIds")List<Long> roleIds, @Param("status")Integer status);
	int removeRefsByRoleId(@Param("roleId")long roleId);
	List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam param);
	List<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam param);
	long countUserRoleRef(UserRoleRefQueryParam param);
	List<String> pageUser(UsersRoleRefQueryParam param);
	int countUser(UsersRoleRefQueryParam param);
	List<UserRoleRefDTO> listUsersRoleRef(UsersRoleRefQueryParam param);
	UserRoleRefPO hasUserUse(@Param("roleId")long roleId);
	int checkUserRole(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleId")long roleId);
	List<String> listUserTenant(@Param("userId")String userId, @Param("appKey")String appKey, @Param("roleKeys")List<String> roleKeys);
}
