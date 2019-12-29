package com.kingbo401.acl.manager.impl;

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
import com.kingbo401.acl.dao.RoleMenuRefDAO;
import com.kingbo401.acl.manager.MenuManager;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.acl.model.entity.MenuDO;
import com.kingbo401.acl.model.entity.param.GetUserMenuParam;
import com.kingbo401.acl.model.entity.param.RoleMenuQueryParam;
import com.kingbo401.iceacl.common.constant.AclConstant;
import com.kingbo401.iceacl.common.enums.MenuShowType;
import com.kingbo401.iceacl.common.model.MenuTreeNode;

@Service
public class MenuManagerImpl implements MenuManager{
	@Autowired
	private MenuDAO menuDAO;
	@Autowired
	private RoleMenuRefDAO roleMenuRefDAO;
	@Autowired
	private RoleManager roleManager;
	
	@Override
	public List<MenuTreeNode> getAppMenuTree(String appKey, String subgroup) {
		Assert.hasText(appKey, "appKey不能为空");
		if (subgroup == null) {
			subgroup = AclConstant.DEF_SUBGROUP;
		}
		List<MenuDO> menuDOs = menuDAO.listMenu(appKey, subgroup);
		return buildMenuTree(menuDOs);
	}
	
	private List<MenuTreeNode> buildMenuTree(List<MenuDO> menuDOs){
		if(CollectionUtils.isEmpty(menuDOs)){
			return null;
		}
		List<MenuDO> rootMenus = Lists.newArrayList();
		Map<Long, List<MenuDO>> menusMap = Maps.newHashMap();
		for (MenuDO menuDO : menuDOs) {
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
	public List<MenuTreeNode> getUserMenuTree(GetUserMenuParam param) {
		Assert.hasText(param.getUserId(), "userId不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		Assert.hasText(param.getTenant(), "tenant不能为空");
		if (param.getSubgroup() == null) {
			param.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		List<MenuDO> menuDOs = menuDAO.listUserMenu(param);
		return buildMenuTree(menuDOs);
	}
	
	@Override
	public List<MenuTreeNode> getRoleMenuTree(RoleMenuQueryParam param) {
		String appKey = param.getAppKey();
		Long roleId = param.getRoleId();
		Assert.notNull(roleManager.getRoleById(appKey, roleId), "角色不存在");
		String subgroup = param.getSubgroup();
		if (subgroup == null) {
			subgroup = AclConstant.DEF_SUBGROUP;
			param.setSubgroup(subgroup);
		}
		List<MenuDO> menuDOs = roleMenuRefDAO.listMenu(param);
		return buildMenuTree(menuDOs);
	}

	@Override
	public MenuDTO createMenu(MenuDTO menuDTO) {
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getName(), "menuName不能为空");
		Assert.hasText(menuDTO.getUrl(), "menuUrl不能为空");
		if (menuDTO.getSubgroup() == null) {
			menuDTO.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		Assert.isTrue(MenuShowType.isValid(menuDTO.getShowType()), "菜单显示类型非法");
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
		BeanUtils.copyProperties(menuDTO, menuDO);
		menuDAO.updateMenu(menuDO);
		return menuDTO;
	}
	
	private boolean updateMenuStatus(MenuDTO menu, int status){
		String appKey = menu.getAppKey();
		Long id = menu.getId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuDO menuDO = menuDAO.getMenuById(id);
		Assert.notNull(menuDO, "菜单不存在");
		Assert.isTrue(appKey.equals(menuDO.getAppKey()), "appkey菜单不匹配");
		menuDO.setStatus(status);
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
	public boolean removeMenu(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeMenu(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeMenu(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_NORMAL);
	}
}
