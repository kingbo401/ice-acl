package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.UserRoleRefDO;
import com.kingbo401.iceacl.model.db.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.param.UsersRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;

public interface UserRoleRefDAO {
	int batchCreate(@Param("list")List<UserRoleRefDO> userRoleRefDOs);
	int updateRefsStatus(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleIds")List<Long> roleIds, @Param("status")Integer status);
	int removeRefsByRoleId(@Param("roleId")long roleId);
	List<UserRoleRefVO> listUserRoleRef(UserRoleRefQueryParam param);
	List<UserRoleRefVO> pageUserRoleRef(UserRoleRefQueryParam param);
	long countUserRoleRef(UserRoleRefQueryParam param);
	List<String> pageUser(UsersRoleRefQueryParam param);
	int countUser(UsersRoleRefQueryParam param);
	List<UserRoleRefVO> listUsersRoleRef(UsersRoleRefQueryParam param);
	UserRoleRefDO hasUserUse(@Param("roleId")long roleId);
	int hasUserRole(@Param("userId")String userId, @Param("tenant") String tenant, @Param("roleId")long roleId);
	List<String> listUserTenant(@Param("userId")String userId, @Param("appKey")String appKey, @Param("roleKeys")List<String> roleKeys);
}
