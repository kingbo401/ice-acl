package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.PermissionGroupDO;
import com.kingbo401.acl.model.entity.PermissionGroupLite;
import com.kingbo401.acl.model.entity.param.PermissionGroupQueryParam;

public interface PermissionGroupDAO {
    int create(PermissionGroupDO permissionGroup);

    int update(PermissionGroupDO permissionGroup);

    List<PermissionGroupDO> getByIds(@Param("ids") List<Long> ids);

    PermissionGroupDO getById(@Param("id") long id);
    
    List<PermissionGroupLite> listPermissionGroupLite(@Param("appKey")String appKey, @Param("tenant")String tenant);

    List<PermissionGroupDO> listPermissionGroup(PermissionGroupQueryParam param);
    
    List<PermissionGroupDO> pagePermissionGroup(PermissionGroupQueryParam param);
    
    long countPermissionGroup(PermissionGroupQueryParam param);
}
