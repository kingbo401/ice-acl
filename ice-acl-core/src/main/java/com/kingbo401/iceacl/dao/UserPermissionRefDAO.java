package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.dto.UserPermissionRefDTO;
import com.kingbo401.iceacl.model.po.UserPermissionRefPO;
import com.kingbo401.iceacl.model.po.param.UserPermissionRefQueryParam;

public interface UserPermissionRefDAO {
	int batchCreate(@Param("list")List<UserPermissionRefPO> userPermissionRefs);
	int updateRefsStatus(@Param("userId")String userId, @Param("tenant") String tenant, @Param("permissionIds")List<Long> permissionIds, @Param("status")Integer status);
	int removeRefsByPermissionId(@Param("permissionId")long permissionId);
	List<UserPermissionRefDTO> listUserPermissionRef(UserPermissionRefQueryParam param);
	List<UserPermissionRefDTO> pageUserPermissionRef(UserPermissionRefQueryParam param);
	long countUserPermissionRef(UserPermissionRefQueryParam param);
	UserPermissionRefPO hasUserUse(@Param("permissionId")long permissionId);
}
