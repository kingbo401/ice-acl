package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.PermissionGroupDO;
import com.kingbo401.iceacl.model.db.RolePermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.RolePermissionGroupRefQueryParam;

public interface RolePermissionGroupRefDAO {

    int batchCreate(@Param("list")List<RolePermissionGroupRefDO> list);

    int updateRefsStatus(@Param("roleId") Long roleId, @Param("groupIds") List<Long> groupIds, @Param("status")Integer status);

    int hasRoleUse(@Param("groupId") Long groupId);

    List<PermissionGroupDO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupDO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    long countPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupDO> getPermissionGroupsByRoleIds(@Param("appKey")String appKey, List<Long> roleIds);
}
