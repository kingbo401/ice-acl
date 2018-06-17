package com.kingbo401.iceacl.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingbo401.iceacl.dao.MenuDAO;
import com.kingbo401.iceacl.manager.MenuManager;
import com.kingbo401.iceacl.model.db.MenuDO;
import com.kingbo401.iceacl.model.dto.MenuDTO;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.model.MenuTreeNode;

@Service
public class MenuManagerImpl implements MenuManager{
	@Autowired
	private MenuDAO menuDAO;
	
	@Override
	public List<MenuTreeNode> getAppMenuTree(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		List<MenuDO> menuDOs = menuDAO.listMenu(appKey, null);
		return buildMenuTree(menuDOs);
	}
	
	private List<MenuTreeNode> buildMenuTree(List<MenuDO> menuDOs){
		if(CollectionUtils.isEmpty(menuDOs)){
			return null;
		}
		List<MenuDO> rootMenus = Lists.newArrayList();
		Map<Long, List<MenuDO>> menusMap = Maps.newHashMap();
		for (MenuDO menuDO : menuDOs) {
			Long menuPid = menuDO.getMenuPid();
			if (menuPid == 0) {
				rootMenus.add(menuDO);
				continue;
			}
			List<MenuDO> list = menusMap.get(menuPid);
			if (list == null) {
				list = Lists.newArrayList();
				menusMap.put(menuPid, list);
			}
			list.add(menuDO);
		}

		List<MenuTreeNode> treeNodeDTOs = Lists.newArrayList();
		//遍历菜单和权限
		for (MenuDO rootMenu : rootMenus) {
			MenuTreeNode menuTreeNode = new MenuTreeNode();
			BeanUtils.copyProperties(rootMenu, menuTreeNode);
			this.recursiveMenu(menuTreeNode, menusMap, 1);
			treeNodeDTOs.add(menuTreeNode);
		}
		return treeNodeDTOs;
	}
	
	private void recursiveMenu(MenuTreeNode treeNodeDTO, Map<Long, List<MenuDO>> menusMap, int level) {
		level++;
		Long menuId = treeNodeDTO.getId();
		List<MenuDO> childMenus = menusMap.get(menuId);
		if (CollectionUtils.isEmpty(childMenus)) {
			return;
		}
		for (MenuDO childMenu : childMenus) {
			MenuTreeNode childTreeNode = new MenuTreeNode();
			BeanUtils.copyProperties(childMenu, childTreeNode);
			childTreeNode.setLevel(level);
			treeNodeDTO.getChildren().add(childTreeNode);
			this.recursiveMenu(childTreeNode, menusMap, level);
		}
	}

	@Override
	public List<MenuTreeNode> listUserMenuTree(String userId, String appKey, String tenant) {
		Assert.hasText(userId, "userId不能为空");
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		List<MenuDO> menuDOs = menuDAO.listUserMenu(userId, appKey, tenant);
		return buildMenuTree(menuDOs);
	}

	@Override
	public MenuDTO createMenu(MenuDTO menuDTO) {
		Assert.notNull(menuDTO);
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getMenuName(), "menuName不能为空");
		Assert.hasText(menuDTO.getMenuUrl(), "menuUrl不能为空");
		Date now = new Date();
		menuDTO.setCreateTime(now);
		menuDTO.setUpdateTime(now);
		menuDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		MenuDO menuDO = new MenuDO();
		BeanUtils.copyProperties(menuDTO, menuDO);
		menuDAO.createMenu(menuDO);
		menuDTO.setId(menuDO.getId());
		return menuDTO;
	}

	@Override
	public MenuDTO updateMenu(MenuDTO menuDTO) {
		Assert.notNull(menuDTO, "参数不能为空");
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getMenuName(), "menuName不能为空");
		Assert.hasText(menuDTO.getMenuUrl(), "menuUrl不能为空");
		Long id = menuDTO.getId();
		Assert.notNull(id, "id不能null");
		MenuDO menuDO = menuDAO.getMenuById(id);
		Assert.notNull(menuDO, "菜单不存在");
		Date now = new Date();
		menuDTO.setUpdateTime(now);
		BeanUtils.copyProperties(menuDTO, menuDO);
		menuDAO.updateMenu(menuDO);
		return menuDTO;
	}
	
	private boolean updateMenuStatus(String appKey, Long id, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuDO menuDO = menuDAO.getMenuById(id);
		Assert.notNull(menuDO, "菜单不存在");
		Assert.isTrue(appKey.equals(menuDO.getAppKey()), "appkey菜单不匹配");
		menuDO.setStatus(status);
		menuDO.setUpdateTime(new Date());
		menuDAO.updateMenu(menuDO);
		return true;
	}

	@Override
	public boolean removeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, IceAclConstant.STATUS_NORMAL);
	}
}
