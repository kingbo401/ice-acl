package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.MenuPO;
import com.kingbo401.iceacl.model.po.param.CheckUserMenuParam;

public interface MenuDAO {
	List<MenuPO> listMenu(@Param("appKey")String appKey, @Param("status")Integer status);

	int createMenu(MenuPO menu);

	int updateMenu(MenuPO menu);

	MenuPO getMenuById(@Param("id")Long id);
	
	List<MenuPO> getMenuByIds(@Param("ids") List<Long> ids);

	List<MenuPO> listMenuByPid(@Param("menuPid") Long menuPid);
	
	List<MenuPO> listUserMenu(@Param("userId") String userId, @Param("appKey") String appKey,@Param("tenant") String tenant);
	
	int checkUserMenu(CheckUserMenuParam param);
}
