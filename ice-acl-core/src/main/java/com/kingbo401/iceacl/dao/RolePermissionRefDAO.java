package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.RolePermissionRefPO;
import com.kingbo401.iceacl.model.po.param.RolePermissionRefQueryParam;

public interface RolePermissionRefDAO {
	
    int batchCreate(@Param("list")List<RolePermissionRefPO> list);

    int updateRefsStatus(@Param("roleId") long roleId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);

    List<PermissionPO> listPermission(RolePermissionRefQueryParam param);
    
    List<PermissionPO> pagePermission(RolePermissionRefQueryParam param);

    long countPermission(RolePermissionRefQueryParam param);
}
