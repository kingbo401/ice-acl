package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.acl.model.entity.UserPermissionGroupRefDO;
import com.kingbo401.acl.model.entity.param.UserPermissionGroupRefQueryParam;

public interface UserPermissionGroupRefDAO {
	int batchCreate(@Param("list")List<UserPermissionGroupRefDO> userPermissionGroupRefs);
	int updateRefsStats(@Param("userId")Long userId, @Param("tenant") String tenant, @Param("groupIds")List<Long> groupIds, @Param("status")Integer status);
	int removeRefsByGroupId(@Param("groupId")long groupId);
	List<UserPermissionGroupRefDTO> listUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	List<UserPermissionGroupRefDTO> pageUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	long countUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	UserPermissionGroupRefDO hasUserUse(@Param("groupId")long groupId);
}
