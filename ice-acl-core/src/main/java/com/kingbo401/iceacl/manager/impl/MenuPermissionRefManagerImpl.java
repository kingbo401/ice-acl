package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.MenuPermissionRefDAO;
import com.kingbo401.iceacl.manager.MenuManager;
import com.kingbo401.iceacl.manager.MenuPermissionRefManager;
import com.kingbo401.iceacl.manager.PermissionManager;
import com.kingbo401.iceacl.model.dto.MenuDTO;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.param.MenuPermissionRefParam;
import com.kingbo401.iceacl.model.po.MenuPermissionRefPO;
import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.param.MenuPermissionRefQueryParam;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;

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
		List<MenuPermissionRefPO> list = new ArrayList<MenuPermissionRefPO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			MenuPermissionRefPO menuPermissionRefPO = new MenuPermissionRefPO();
			menuPermissionRefPO.setPermissionId(permissionId);
			menuPermissionRefPO.setMenuId(menuId);
			menuPermissionRefPO.setCreateTime(now);
			menuPermissionRefPO.setUpdateTime(now);
			menuPermissionRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(menuPermissionRefPO);
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
		String appKey = param.getAppKey();
		MenuPermissionRefQueryParam menuPermissionRefQueryParam = new MenuPermissionRefQueryParam();
		menuPermissionRefQueryParam.setMenuId(menuId);
		if(!param.isMultiApp()){
			menuPermissionRefQueryParam.setPermissionAppKey(appKey);
		}
		List<PermissionPO> permissionPOs =  menuPermissionRefDAO.listPermission(menuPermissionRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionPOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionPO permissionPO : permissionPOs){
				permissionIdsRemove.add(permissionPO.getId());
			}
			menuPermissionRefDAO.updateRefsStatus(menuId, permissionIdsRemove, IceAclConstant.STATUS_REMOVE);
		}
		
		List<MenuPermissionRefPO> list = new ArrayList<MenuPermissionRefPO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			MenuPermissionRefPO menuPermissionRefPO = new MenuPermissionRefPO();
			menuPermissionRefPO.setPermissionId(permissionId);
			menuPermissionRefPO.setMenuId(menuId);
			menuPermissionRefPO.setCreateTime(now);
			menuPermissionRefPO.setUpdateTime(now);
			menuPermissionRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(menuPermissionRefPO);
		}
		menuPermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeMenuPermissionRef(MenuPermissionRefParam param) {
		checkMenuPermissionIdRefParam(param);
		menuPermissionRefDAO.updateRefsStatus(param.getMenuId(), param.getPermissionIds(), IceAclConstant.STATUS_NORMAL);
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
		List<PermissionPO> permissionPOs = menuPermissionRefDAO.listPermission(param);
		return BizUtils.buildPermissionDTOs(permissionPOs);
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
		List<PermissionPO> permissionPOs = menuPermissionRefDAO.pagePermission(param);
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionPOs));
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
			if(!param.isMultiApp()){
				for(PermissionDTO permissionDTO : permissionDTOs){
					Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appkey不匹配:" + permissionDTO.getId());
				}
			}
		}
	}
}
