package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.MenuTreeNode;
import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.acl.model.entity.param.GetUserMenuParam;
import com.kingbo401.acl.model.entity.param.RoleMenuQueryParam;

public interface MenuManager {
	List<MenuTreeNode> getAppMenuTree(String appKey, String subgroup);

	List<MenuTreeNode> getUserMenuTree(GetUserMenuParam param);
	
	List<MenuTreeNode> getRoleMenuTree(RoleMenuQueryParam param);
	
	MenuDTO create(MenuDTO menu);
	
	MenuDTO update(MenuDTO menu);

	MenuDTO getById(String appKey, Long id);
	
	MenuDTO getByKey(String appKey, String menuKey, String subgroup);
	
	boolean remove(MenuDTO menu);
	
	boolean freeze(MenuDTO menu);
	
	boolean unfreeze(MenuDTO menu);
}
