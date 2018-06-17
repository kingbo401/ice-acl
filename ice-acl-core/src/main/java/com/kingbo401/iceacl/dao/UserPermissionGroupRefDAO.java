package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.UserPermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserPermissionGroupRefVO;

public interface UserPermissionGroupRefDAO {
	int batchCreate(@Param("list")List<UserPermissionGroupRefDO> userPermissionGroupRefDOs);
	int updateRefsStats(@Param("userId")String userId, @Param("tenant") String tenant, @Param("groupIds")List<Long> groupIds, @Param("status")Integer status);
	int removeRefsByGroupId(@Param("groupId")long groupId);
	List<UserPermissionGroupRefVO> listUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	List<UserPermissionGroupRefVO> pageUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	long countUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	UserPermissionGroupRefDO hasUserUse(@Param("groupId")long groupId);
}
