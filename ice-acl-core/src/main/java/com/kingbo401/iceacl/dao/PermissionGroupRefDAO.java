package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.PermissionGroupRefPO;
import com.kingbo401.iceacl.model.po.param.PermissionGroupRefQueryParam;

public interface PermissionGroupRefDAO {
    int batchCreate(@Param("list")List<PermissionGroupRefPO> list);

    int updateRefsStatus(@Param("groupId") long groupId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);

    List<PermissionPO> listPermission(PermissionGroupRefQueryParam param);
    
    List<PermissionPO> pagePermission(PermissionGroupRefQueryParam param);

    long countPermission(PermissionGroupRefQueryParam param);
}
