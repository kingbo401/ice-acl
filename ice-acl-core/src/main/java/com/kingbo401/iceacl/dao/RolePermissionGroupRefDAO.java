package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.PermissionGroupPO;
import com.kingbo401.iceacl.model.po.RolePermissionGroupRefPO;
import com.kingbo401.iceacl.model.po.param.RolePermissionGroupRefQueryParam;

public interface RolePermissionGroupRefDAO {

    int batchCreate(@Param("list")List<RolePermissionGroupRefPO> list);

    int updateRefsStatus(@Param("roleId") Long roleId, @Param("groupIds") List<Long> groupIds, @Param("status")Integer status);

    int hasRoleUse(@Param("groupId") Long groupId);

    List<PermissionGroupPO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupPO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    long countPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<PermissionGroupPO> getPermissionGroupsByRoleIds(@Param("appKey")String appKey, List<Long> roleIds);
}
