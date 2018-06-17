package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.MenuDO;
import com.kingbo401.iceacl.model.db.RoleMenuRefDO;
import com.kingbo401.iceacl.model.db.param.RoleMenuQueryParam;

public interface RoleMenuRefDAO {
	int batchCreate(@Param("list")List<RoleMenuRefDO> refs);
	int updateRefsStatus(@Param("roleId")Long roleId, @Param("menuIds")List<Long> menuIds, @Param("status")Integer status);
	List<MenuDO> listMenu(RoleMenuQueryParam param);
}
