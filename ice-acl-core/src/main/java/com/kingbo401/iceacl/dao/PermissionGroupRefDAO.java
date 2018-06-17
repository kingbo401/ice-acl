package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.PermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.PermissionGroupRefQueryParam;

public interface PermissionGroupRefDAO {
    int batchCreate(@Param("list")List<PermissionGroupRefDO> list);

    int updateRefsStatus(@Param("groupId") long groupId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);

    List<PermissionDO> listPermission(PermissionGroupRefQueryParam param);
    
    List<PermissionDO> pagePermission(PermissionGroupRefQueryParam param);

    long countPermission(PermissionGroupRefQueryParam param);
}
