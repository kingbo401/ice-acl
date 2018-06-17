package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.RolePermissionRefDO;
import com.kingbo401.iceacl.model.db.param.RolePermissionRefQueryParam;

public interface RolePermissionRefDAO {
	
    int batchCreate(@Param("list")List<RolePermissionRefDO> list);

    int updateRefsStatus(@Param("roleId") long roleId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);

    List<PermissionDO> listPermission(RolePermissionRefQueryParam param);
    
    List<PermissionDO> pagePermission(RolePermissionRefQueryParam param);

    long countPermission(RolePermissionRefQueryParam param);
}
