package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.dto.UserPermissionRefDTO;
import com.kingbo401.acl.model.entity.UserPermissionRefDO;
import com.kingbo401.acl.model.entity.param.UserPermissionRefQueryParam;

public interface UserPermissionRefDAO {
	int batchCreate(@Param("list")List<UserPermissionRefDO> userPermissionRefs);
	int updateRefsStatus(@Param("userId")Long userId, @Param("tenant") String tenant, @Param("permissionIds")List<Long> permissionIds, @Param("status")Integer status);
	int removeRefsByPermissionId(@Param("permissionId")long permissionId);
	List<UserPermissionRefDTO> listUserPermissionRef(UserPermissionRefQueryParam param);
	List<UserPermissionRefDTO> pageUserPermissionRef(UserPermissionRefQueryParam param);
	long countUserPermissionRef(UserPermissionRefQueryParam param);
	UserPermissionRefDO hasUserUse(@Param("permissionId")long permissionId);
}
