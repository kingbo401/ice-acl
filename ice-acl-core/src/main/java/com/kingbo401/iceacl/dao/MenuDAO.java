package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.MenuDO;

public interface MenuDAO {
	List<MenuDO> listMenu(@Param("appKey")String appKey, @Param("status")Integer status);

	int createMenu(MenuDO menuDO);

	int updateMenu(MenuDO menuDO);

	MenuDO getMenuById(@Param("id")Long id);
	
	List<MenuDO> getMenuByIds(@Param("ids") List<Long> ids);

	List<MenuDO> listMenuByPid(@Param("menuPid") Long menuPid);
	
	List<MenuDO> listUserMenu(@Param("userId") String userId, @Param("appKey") String appKey,@Param("tenant") String tenant);
}
