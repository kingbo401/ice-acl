package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.MenuPO;
import com.kingbo401.iceacl.model.po.RoleMenuRefPO;
import com.kingbo401.iceacl.model.po.param.RoleMenuQueryParam;

public interface RoleMenuRefDAO {
	int batchCreate(@Param("list")List<RoleMenuRefPO> refs);
	int updateRefsStatus(@Param("roleId")Long roleId, @Param("menuIds")List<Long> menuIds, @Param("status")Integer status);
	List<MenuPO> listMenu(RoleMenuQueryParam param);
}
