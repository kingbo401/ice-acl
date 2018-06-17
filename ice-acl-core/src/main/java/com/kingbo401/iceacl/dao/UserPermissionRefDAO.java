package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.UserPermissionRefDO;
import com.kingbo401.iceacl.model.db.param.UserPermissionRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserPermissionRefVO;

public interface UserPermissionRefDAO {
	int batchCreate(@Param("list")List<UserPermissionRefDO> userPermissionRefDOs);
	int updateRefsStatus(@Param("userId")String userId, @Param("tenant") String tenant, @Param("permissionIds")List<Long> permissionIds, @Param("status")Integer status);
	int removeRefsByPermissionId(@Param("permissionId")long permissionId);
	List<UserPermissionRefVO> listUserPermissionRef(UserPermissionRefQueryParam param);
	List<UserPermissionRefVO> pageUserPermissionRef(UserPermissionRefQueryParam param);
	long countUserPermissionRef(UserPermissionRefQueryParam param);
	UserPermissionRefDO hasUserUse(@Param("permissionId")long permissionId);
}
