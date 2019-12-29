package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.MenuPermissionRefDAO;
import com.kingbo401.acl.manager.MenuManager;
import com.kingbo401.acl.manager.MenuPermissionRefManager;
import com.kingbo401.acl.manager.PermissionManager;
import com.kingbo401.acl.model.dto.MenuDTO;
import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.param.MenuPermissionRefParam;
import com.kingbo401.acl.model.entity.MenuPermissionRefDO;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.param.MenuPermissionRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class MenuPermissionRefManagerImpl implements MenuPermissionRefManager{
	@Autowired
	private MenuManager menuManager;
	@Autowired
	private PermissionManager permissionManager;
	@Autowired
	private MenuPermissionRefDAO menuPermissionRefDAO;
	
	@Override
	public boolean addMenuPermissionRef(MenuPermissionRefParam param) {
		Assert.notNull(param, "参数不能为空");
		Long menuId = param.getMenuId();
		Assert.notNull(menuId, "menuId不能为空");
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		checkMenuPermissionIdRefParam(param);
		List<MenuPermissionRefDO> list = new ArrayList<MenuPermissionRefDO>();
		for(Long permissionId : permissionIds){
			MenuPermissionRefDO menuPermissionRefDO = new MenuPermissionRefDO();
			menuPermissionRefDO.setPermissionId(permissionId);
			menuPermissionRefDO.setMenuId(menuId);
			menuPermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(menuPermissionRefDO);
		}
		menuPermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean updateMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		Long menuId = param.getMenuId();
		MenuPermissionRefQueryParam menuPermissionRefQueryParam = new MenuPermissionRefQueryParam();
		menuPermissionRefQueryParam.setMenuId(menuId);
		List<PermissionDO> permissionDOs =  menuPermissionRefDAO.listPermission(menuPermissionRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionDOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionDO permissionDO : permissionDOs){
				permissionIdsRemove.add(permissionDO.getId());
			}
			menuPermissionRefDAO.updateRefsStatus(menuId, permissionIdsRemove, AclConstant.STATUS_REMOVE);
		}
		
		List<MenuPermissionRefDO> list = new ArrayList<MenuPermissionRefDO>();
		for(Long permissionId : permissionIds){
			MenuPermissionRefDO menuPermissionRefDO = new MenuPermissionRefDO();
			menuPermissionRefDO.setPermissionId(permissionId);
			menuPermissionRefDO.setMenuId(menuId);
			menuPermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(menuPermissionRefDO);
		}
		menuPermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), AclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), AclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), AclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public List<PermissionDTO> listMenuPermission(MenuPermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long menuId = param.getMenuId();
		Assert.notNull(menuId, "menuId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		MenuDTO menuDTO = menuManager.getMenu(appKey, menuId);
		Assert.notNull(menuDTO, "菜单不存在");
		List<PermissionDO> permissionDOs = menuPermissionRefDAO.listPermission(param);
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public PageVO<PermissionDTO> pageMenuPermission(MenuPermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long menuId = param.getMenuId();
		Assert.notNull(menuId, "menuId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		MenuDTO menuDTO = menuManager.getMenu(appKey, menuId);
		Assert.notNull(menuDTO, "菜单不存在");
		PageVO<PermissionDTO> pageVO = new PageVO<PermissionDTO>(param);
		if(param.isReturnTotalCount()){
			long total = menuPermissionRefDAO.countPermission(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<PermissionDO> permissionDOs = menuPermissionRefDAO.pagePermission(param);
		pageVO.setItems(BizUtil.buildPermissionDTOs(permissionDOs));
		return pageVO;
	}

	private void checkMenuPermissionIdRefParam(MenuPermissionRefParam param){
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long menuId = param.getMenuId();
		Assert.notNull(menuId, "menuId不能为空");
		MenuDTO menuDTO = menuManager.getMenu(appKey, menuId);
		Assert.notNull(menuDTO, "菜单不存在");
		List<Long> permissionIds = param.getPermissionIds();
		if(CollectionUtil.isNotEmpty(permissionIds)){
			List<PermissionDTO> permissionDTOs = permissionManager.getPermissionByIds(permissionIds);
			Assert.notEmpty(permissionDTOs, "权限不存在");
			for(PermissionDTO permissionDTO : permissionDTOs){
				Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appkey不匹配:" + permissionDTO.getId());
			}
		}
	}
}
