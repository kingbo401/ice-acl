package com.kingbo401.acl.manager.impl;

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
import com.kingbo401.acl.dao.MenuDAO;
import com.kingbo401.acl.manager.MenuManager;
import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.acl.model.entity.MenuDO;

import kingbo401.iceacl.common.constant.AclConstant;
import kingbo401.iceacl.common.enums.MenuShowType;
import kingbo401.iceacl.common.model.MenuTreeNode;

@Service
public class MenuManagerImpl implements MenuManager{
	@Autowired
	private MenuDAO menuDAO;
	
	@Override
	public List<MenuTreeNode> getAppMenuTree(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		List<MenuDO> menuPOs = menuDAO.listMenu(appKey, null);
		return buildMenuTree(menuPOs);
	}
	
	private List<MenuTreeNode> buildMenuTree(List<MenuDO> menuPOs){
		if(CollectionUtils.isEmpty(menuPOs)){
			return null;
		}
		List<MenuDO> rootMenus = Lists.newArrayList();
		Map<Long, List<MenuDO>> menusMap = Maps.newHashMap();
		for (MenuDO menuDO : menuPOs) {
			Long menuPid = menuDO.getPid();
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
		List<MenuDO> menuPOs = menuDAO.listUserMenu(userId, appKey, tenant);
		return buildMenuTree(menuPOs);
	}

	@Override
	public MenuDTO createMenu(MenuDTO menuDTO) {
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getName(), "menuName不能为空");
		Assert.hasText(menuDTO.getUrl(), "menuUrl不能为空");
		Assert.isTrue(MenuShowType.isValid(menuDTO.getShowType()), "菜单显示类型非法");
		Date now = new Date();
		menuDTO.setCreateTime(now);
		menuDTO.setUpdateTime(now);
		menuDTO.setStatus(AclConstant.STATUS_NORMAL);
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
		Assert.hasText(menuDTO.getName(), "menuName不能为空");
		Assert.hasText(menuDTO.getUrl(), "menuUrl不能为空");
		Assert.isTrue(MenuShowType.isValid(menuDTO.getShowType()), "菜单显示类型非法");
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
	public MenuDTO getMenu(String appKey, Long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuDO menuDO = menuDAO.getMenuById(id);
		if(menuDO == null){
			return null;
		}
		Assert.isTrue(appKey.equals(menuDO.getAppKey()), "appkey菜单不匹配");
		MenuDTO menuDTO = new MenuDTO();
		BeanUtils.copyProperties(menuDO, menuDTO);
		return menuDTO;
	}

	@Override
	public boolean removeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeMenu(String appKey, Long id) {
		return updateMenuStatus(appKey, id, AclConstant.STATUS_NORMAL);
	}
}
