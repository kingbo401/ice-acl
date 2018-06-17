package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.iceacl.model.dto.MenuDTO;

import kingbo401.iceacl.common.model.MenuTreeNode;

public interface MenuManager {
	List<MenuTreeNode> getAppMenuTree(String appKey);

	List<MenuTreeNode> listUserMenuTree(String userId, String appKey, String tenant);
	
	MenuDTO createMenu(MenuDTO menu);
	
	MenuDTO updateMenu(MenuDTO menu);

	boolean removeMenu(String appKey, Long id);
	
	boolean freezeMenu(String appKey, Long id);
	
	boolean unfreezeMenu(String appKey, Long id);
}
