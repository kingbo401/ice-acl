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
import com.kingbo401.iceacl.model.dto.MenuDTO;
import com.kingbo401.iceacl.model.po.MenuPO;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.enums.MenuShowType;
import kingbo401.iceacl.common.model.MenuTreeNode;

@Service
public class MenuManagerImpl implements MenuManager{
	@Autowired
	private MenuDAO menuDAO;
	
	@Override
	public List<MenuTreeNode> getAppMenuTree(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		List<MenuPO> menuPOs = menuDAO.listMenu(appKey, null);
		return buildMenuTree(menuPOs);
	}
	
	private List<MenuTreeNode> buildMenuTree(List<MenuPO> menuPOs){
		if(CollectionUtils.isEmpty(menuPOs)){
			return null;
		}
		List<MenuPO> rootMenus = Lists.newArrayList();
		Map<Long, List<MenuPO>> menusMap = Maps.newHashMap();
		for (MenuPO menuPO : menuPOs) {
			Long menuPid = menuPO.getMenuPid();
			if (menuPid == 0) {
				rootMenus.add(menuPO);
				continue;
			}
			List<MenuPO> list = menusMap.get(menuPid);
			if (list == null) {
				list = Lists.newArrayList();
				menusMap.put(menuPid, list);
			}
			list.add(menuPO);
		}

		List<MenuTreeNode> treeNodeDTOs = Lists.newArrayList();
		//遍历菜单和权限
		for (MenuPO rootMenu : rootMenus) {
			MenuTreeNode menuTreeNode = new MenuTreeNode();
			BeanUtils.copyProperties(rootMenu, menuTreeNode);
			this.recursiveMenu(menuTreeNode, menusMap, 1);
			treeNodeDTOs.add(menuTreeNode);
		}
		return treeNodeDTOs;
	}
	
	private void recursiveMenu(MenuTreeNode treeNodeDTO, Map<Long, List<MenuPO>> menusMap, int level) {
		level++;
		Long menuId = treeNodeDTO.getId();
		List<MenuPO> childMenus = menusMap.get(menuId);
		if (CollectionUtils.isEmpty(childMenus)) {
			return;
		}
		for (MenuPO childMenu : childMenus) {
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
		List<MenuPO> menuPOs = menuDAO.listUserMenu(userId, appKey, tenant);
		return buildMenuTree(menuPOs);
	}

	@Override
	public MenuDTO createMenu(MenuDTO menuDTO) {
		Assert.notNull(menuDTO);
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getMenuName(), "menuName不能为空");
		Assert.hasText(menuDTO.getMenuUrl(), "menuUrl不能为空");
		Assert.isTrue(MenuShowType.isValid(menuDTO.getMenuShowType()), "菜单显示类型非法");
		Date now = new Date();
		menuDTO.setCreateTime(now);
		menuDTO.setUpdateTime(now);
		menuDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		MenuPO menuPO = new MenuPO();
		BeanUtils.copyProperties(menuDTO, menuPO);
		menuDAO.createMenu(menuPO);
		menuDTO.setId(menuPO.getId());
		return menuDTO;
	}

	@Override
	public MenuDTO updateMenu(MenuDTO menuDTO) {
		Assert.notNull(menuDTO, "参数不能为空");
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getMenuName(), "menuName不能为空");
		Assert.hasText(menuDTO.getMenuUrl(), "menuUrl不能为空");
		Assert.isTrue(MenuShowType.isValid(menuDTO.getMenuShowType()), "菜单显示类型非法");
		Long id = menuDTO.getId();
		Assert.notNull(id, "id不能null");
		MenuPO menuPO = menuDAO.getMenuById(id);
		Assert.notNull(menuPO, "菜单不存在");
		Date now = new Date();
		menuDTO.setUpdateTime(now);
		BeanUtils.copyProperties(menuDTO, menuPO);
		menuDAO.updateMenu(menuPO);
		return menuDTO;
	}
	
	private boolean updateMenuStatus(String appKey, Long id, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuPO menuPO = menuDAO.getMenuById(id);
		Assert.notNull(menuPO, "菜单不存在");
		Assert.isTrue(appKey.equals(menuPO.getAppKey()), "appkey菜单不匹配");
		menuPO.setStatus(status);
		menuPO.setUpdateTime(new Date());
		menuDAO.updateMenu(menuPO);
		return true;
	}

	@Override
	public MenuDTO getMenu(String appKey, Long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuPO menuPO = menuDAO.getMenuById(id);
		if(menuPO == null){
			return null;
		}
		Assert.isTrue(appKey.equals(menuPO.getAppKey()), "appkey菜单不匹配");
		MenuDTO menuDTO = new MenuDTO();
		BeanUtils.copyProperties(menuPO, menuDTO);
		return menuDTO;
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
