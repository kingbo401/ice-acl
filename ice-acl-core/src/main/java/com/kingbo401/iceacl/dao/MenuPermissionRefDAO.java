package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.MenuPermissionRefDO;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.param.MenuPermissionRefQueryParam;

public interface MenuPermissionRefDAO {
	int batchCreate(@Param("list")List<MenuPermissionRefDO> list);
    int updateRefsStatus(@Param("menuId") long menuId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);
    List<PermissionDO> listPermission(MenuPermissionRefQueryParam param);
    List<PermissionDO> pagePermission(MenuPermissionRefQueryParam param);
    long countPermission(MenuPermissionRefQueryParam param);
    List<Long> listMenuIds(@Param("permissionId")Long permissionId);
}
