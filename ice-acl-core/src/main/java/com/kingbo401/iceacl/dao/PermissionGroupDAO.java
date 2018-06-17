package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.PermissionGroupDO;
import com.kingbo401.iceacl.model.db.PermissionGroupLite;
import com.kingbo401.iceacl.model.db.param.PermissionGroupQueryParam;

public interface PermissionGroupDAO {
    int create(PermissionGroupDO permissionGroupDO);

    int update(PermissionGroupDO permissionGroupDO);

    List<PermissionGroupDO> getByIds(@Param("ids") List<Long> ids);

    PermissionGroupDO getById(@Param("id") long id);
    
    List<PermissionGroupLite> listPermissionGroupLite(@Param("appKey")String appKey, @Param("tenant")String tenant);

    List<PermissionGroupDO> listPermissionGroup(PermissionGroupQueryParam param);
    
    List<PermissionGroupDO> pagePermissionGroup(PermissionGroupQueryParam param);
    
    long countPermissionGroup(PermissionGroupQueryParam param);
}
