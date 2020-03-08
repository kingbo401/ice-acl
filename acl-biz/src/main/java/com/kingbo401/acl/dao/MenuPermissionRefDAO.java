package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.MenuPermissionRefDO;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.param.MenuPermissionRefQueryParam;

public interface MenuPermissionRefDAO {
	int batchCreate(@Param("list")List<MenuPermissionRefDO> list);
    int updateRefsStatus(@Param("menuId") long menuId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);
    List<PermissionDO> listPermission(MenuPermissionRefQueryParam param);
    List<PermissionDO> pagePermission(MenuPermissionRefQueryParam param);
    long countPermission(MenuPermissionRefQueryParam param);
    List<Long> listMenuIds(@Param("permissionId")Long permissionId);
}
