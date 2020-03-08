package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.MenuDO;
import com.kingbo401.acl.model.entity.param.CheckUserMenuParam;
import com.kingbo401.acl.model.entity.param.GetUserMenuParam;

public interface MenuDAO {
	List<MenuDO> listMenu(@Param("appKey")String appKey, @Param("subgroup")String subgroup);
	
	int create(MenuDO menu);

	int update(MenuDO menu);
	
	MenuDO getByKey0(@Param("appKey")String appKey, @Param("menuKey")String menuKey, @Param("subgroup")String subgroup);
	
	MenuDO getById(@Param("id")Long id);
	
	List<MenuDO> getByIds(@Param("ids") List<Long> ids);

	List<MenuDO> listByPid(@Param("menuPid") Long menuPid);
	
	List<MenuDO> listUserMenu(GetUserMenuParam param);
	
	int checkUserMenu(CheckUserMenuParam param);
}
