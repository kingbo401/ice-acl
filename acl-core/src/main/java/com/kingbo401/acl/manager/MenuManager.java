package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.iceacl.common.model.MenuTreeNode;

public interface MenuManager {
	List<MenuTreeNode> getAppMenuTree(String appKey);

	List<MenuTreeNode> listUserMenuTree(String userId, String appKey, String tenant);
	
	MenuDTO createMenu(MenuDTO menu);
	
	MenuDTO updateMenu(MenuDTO menu);

	MenuDTO getMenu(String appKey, Long id);
	
	boolean removeMenu(MenuDTO menu);
	
	boolean freezeMenu(MenuDTO menu);
	
	boolean unfreezeMenu(MenuDTO menu);
}
