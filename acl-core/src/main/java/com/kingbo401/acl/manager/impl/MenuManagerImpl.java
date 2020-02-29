package com.kingbo401.acl.manager.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.enums.MenuShowType;
import com.kingbo401.acl.common.model.MenuTreeNode;
import com.kingbo401.acl.dao.MenuDAO;
import com.kingbo401.acl.dao.RoleMenuRefDAO;
import com.kingbo401.acl.manager.MenuManager;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.acl.model.entity.MenuDO;
import com.kingbo401.acl.model.entity.param.GetUserMenuParam;
import com.kingbo401.acl.model.entity.param.RoleMenuQueryParam;
import com.kingbo401.commons.encrypt.SecurityUtil;

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
		Assert.notNull(roleManager.getById(appKey, roleId), "角色不存在");
		String subgroup = param.getSubgroup();
		if (subgroup == null) {
			subgroup = AclConstant.DEF_SUBGROUP;
			param.setSubgroup(subgroup);
		}
		List<MenuDO> menuDOs = roleMenuRefDAO.listMenu(param);
		return buildMenuTree(menuDOs);
	}

	@Override
	public MenuDTO create(MenuDTO menuDTO) {
		String appKey = menuDTO.getAppKey();
		String name = menuDTO.getName();
		String url = menuDTO.getUrl();
		String menuKey = menuDTO.getMenuKey();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(name, "name不能为空");
		Assert.hasText(url, "url不能为空");
		if (StringUtils.isBlank(menuKey)) {
			menuKey = SecurityUtil.getUUID();
			menuDTO.setMenuKey(menuKey);
		}
		if (menuDTO.getSubgroup() == null) {
			menuDTO.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		if (menuDTO.getIdx() == null) {
			menuDTO.setIdx(0);
		}
		Assert.isTrue(MenuShowType.isValid(menuDTO.getShowType()), "菜单显示类型非法");
		menuDTO.setStatus(AclConstant.STATUS_NORMAL);
		
		MenuDO menuDO = menuDAO.getByKey0(appKey, menuKey, menuDTO.getSubgroup());
		if (menuDO != null) {
			menuDTO.setId(menuDO.getId());
			BeanUtils.copyProperties(menuDTO, menuDO);
			menuDAO.update(menuDO);
		} else {
			menuDO = new MenuDO();
			BeanUtils.copyProperties(menuDTO, menuDO);
			menuDAO.create(menuDO);
			menuDTO.setId(menuDO.getId());
		}
		return menuDTO;
	}

	@Override
	public MenuDTO update(MenuDTO menuDTO) {
		Assert.notNull(menuDTO, "参数不能为空");
		Assert.hasText(menuDTO.getAppKey(), "appKey不能为空");
		Assert.hasText(menuDTO.getName(), "menuName不能为空");
		Assert.hasText(menuDTO.getUrl(), "menuUrl不能为空");
		Assert.isTrue(MenuShowType.isValid(menuDTO.getShowType()), "菜单显示类型非法");
		Long id = menuDTO.getId();
		Assert.notNull(id, "id不能null");
		MenuDO menuDO = menuDAO.getById(id);
		Assert.notNull(menuDO, "菜单不存在");
		BeanUtils.copyProperties(menuDTO, menuDO);
		menuDAO.update(menuDO);
		return menuDTO;
	}
	
	private boolean updateMenuStatus(MenuDTO menu, int status){
		String appKey = menu.getAppKey();
		Long id = menu.getId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuDO menuDO = menuDAO.getById(id);
		Assert.notNull(menuDO, "菜单不存在");
		Assert.isTrue(appKey.equals(menuDO.getAppKey()), "appkey菜单不匹配");
		menuDO.setStatus(status);
		menuDAO.update(menuDO);
		return true;
	}

	@Override
	public MenuDTO getById(String appKey, Long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "id不能为空");
		MenuDO menuDO = menuDAO.getById(id);
		if(menuDO == null){
			return null;
		}
		Assert.isTrue(appKey.equals(menuDO.getAppKey()), "appkey菜单不匹配");
		MenuDTO menuDTO = new MenuDTO();
		BeanUtils.copyProperties(menuDO, menuDTO);
		return menuDTO;
	}

	@Override
	public MenuDTO getByKey(String appKey, String menuKey, String subgroup) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(menuKey, "menuKey不能为空");
		if (subgroup == null) {
			subgroup = AclConstant.DEF_SUBGROUP;
		}
		MenuDO menuDO = menuDAO.getByKey0(appKey, menuKey, subgroup);
		if(menuDO == null || menuDO.getStatus().equals(AclConstant.STATUS_REMOVE)){
			return null;
		}
		MenuDTO menuDTO = new MenuDTO();
		BeanUtils.copyProperties(menuDO, menuDTO);
		return menuDTO;
	}

	@Override
	public boolean remove(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freeze(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreeze(MenuDTO menu) {
		return updateMenuStatus(menu, AclConstant.STATUS_NORMAL);
	}
}
