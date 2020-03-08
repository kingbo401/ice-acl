package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.RolePermissionRefDO;
import com.kingbo401.acl.model.entity.param.RolePermissionRefQueryParam;

public interface RolePermissionRefDAO {
	
    int batchCreate(@Param("list")List<RolePermissionRefDO> list);

    int updateRefsStatus(@Param("roleId") long roleId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);

    List<PermissionDO> listPermission(RolePermissionRefQueryParam param);
    
    List<PermissionDO> pagePermission(RolePermissionRefQueryParam param);

    long countPermission(RolePermissionRefQueryParam param);
}
