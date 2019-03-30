package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.PermissionGroupPO;
import com.kingbo401.iceacl.model.po.PermissionGroupLite;
import com.kingbo401.iceacl.model.po.param.PermissionGroupQueryParam;

public interface PermissionGroupDAO {
    int create(PermissionGroupPO permissionGroup);

    int update(PermissionGroupPO permissionGroup);

    List<PermissionGroupPO> getByIds(@Param("ids") List<Long> ids);

    PermissionGroupPO getById(@Param("id") long id);
    
    List<PermissionGroupLite> listPermissionGroupLite(@Param("appKey")String appKey, @Param("tenant")String tenant);

    List<PermissionGroupPO> listPermissionGroup(PermissionGroupQueryParam param);
    
    List<PermissionGroupPO> pagePermissionGroup(PermissionGroupQueryParam param);
    
    long countPermissionGroup(PermissionGroupQueryParam param);
}
