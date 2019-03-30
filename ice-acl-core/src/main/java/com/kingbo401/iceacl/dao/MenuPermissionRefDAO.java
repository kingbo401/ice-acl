package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.MenuPermissionRefPO;
import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.param.MenuPermissionRefQueryParam;

public interface MenuPermissionRefDAO {
	int batchCreate(@Param("list")List<MenuPermissionRefPO> list);
    int updateRefsStatus(@Param("menuId") long menuId, @Param("permissionIds") List<Long> permissionIds, @Param("status")Integer status);
    List<PermissionPO> listPermission(MenuPermissionRefQueryParam param);
    List<PermissionPO> pagePermission(MenuPermissionRefQueryParam param);
    long countPermission(MenuPermissionRefQueryParam param);
    List<Long> listMenuIds(@Param("permissionId")Long permissionId);
}
