package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.iceacl.model.po.UserPermissionGroupRefPO;
import com.kingbo401.iceacl.model.po.param.UserPermissionGroupRefQueryParam;

public interface UserPermissionGroupRefDAO {
	int batchCreate(@Param("list")List<UserPermissionGroupRefPO> userPermissionGroupRefs);
	int updateRefsStats(@Param("userId")String userId, @Param("tenant") String tenant, @Param("groupIds")List<Long> groupIds, @Param("status")Integer status);
	int removeRefsByGroupId(@Param("groupId")long groupId);
	List<UserPermissionGroupRefDTO> listUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	List<UserPermissionGroupRefDTO> pageUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	long countUserPermissionGroupRef(UserPermissionGroupRefQueryParam param);
	UserPermissionGroupRefPO hasUserUse(@Param("groupId")long groupId);
}
