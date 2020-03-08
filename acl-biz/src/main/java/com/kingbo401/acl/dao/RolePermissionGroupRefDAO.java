package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.PermissionGroupDO;
import com.kingbo401.acl.model.entity.RolePermissionGroupRefDO;
import com.kingbo401.acl.model.entity.param.RolePermissionGroupRefQueryParam;

public interface RolePermissionGroupRefDAO {

    int batchCreate(@Param("list")List<RolePermissionGroupRefDO> list);

    int updateRefsStatus(@Param("roleId") Long roleId, @Param("groupIds") List<Long> groupIds, @Param("status")Integer status);

    int hasRoleUse(@Param("groupId") Long groupId);

    List<PermissionGroupDO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupDO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    long countPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupDO> getPermissionGroupsByRoleIds(List<Long> roleIds);
}
