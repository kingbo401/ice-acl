package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kingbo401.iceacl.common.constant.IceAclConstant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.RolePermissionGroupRefDAO;
import com.kingbo401.iceacl.manager.PermissionGroupManager;
import com.kingbo401.iceacl.manager.RoleManager;
import com.kingbo401.iceacl.manager.RolePermissionGroupRefManager;
import com.kingbo401.iceacl.manager.UserRoleRefManager;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.UserRoleRefDTO;
import com.kingbo401.iceacl.model.dto.param.RolePermissionGroupRefParam;
import com.kingbo401.iceacl.model.po.PermissionGroupPO;
import com.kingbo401.iceacl.model.po.RolePermissionGroupRefPO;
import com.kingbo401.iceacl.model.po.param.RolePermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.po.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.utils.BizUtils;

@Service
public class RolePermissionGroupRefManagerImpl implements RolePermissionGroupRefManager{
	@Autowired
	private RolePermissionGroupRefDAO rolePermissionGroupRefDAO;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private UserRoleRefManager userRoleRefManager;
	
	@Override
	public boolean addRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		checkRolePermissionGroupRefParam(param);
		List<RolePermissionGroupRefPO> list = new ArrayList<RolePermissionGroupRefPO>();
		Date now = new Date();
		for(Long groupId : groupIds){
			RolePermissionGroupRefPO rolePermissionGroupRefPO = new RolePermissionGroupRefPO();
			rolePermissionGroupRefPO.setGroupId(groupId);
			rolePermissionGroupRefPO.setRoleId(param.getRoleId());
			rolePermissionGroupRefPO.setCreateTime(now);
			rolePermissionGroupRefPO.setUpdateTime(now);
			rolePermissionGroupRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(rolePermissionGroupRefPO);
		}
		rolePermissionGroupRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean updateRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		checkRolePermissionGroupRefParam(param);
		
		RolePermissionGroupRefQueryParam rolePermissionGroupRefQueryParam = new RolePermissionGroupRefQueryParam();
		rolePermissionGroupRefQueryParam.setRoleId(param.getRoleId());
		if(!param.isMultiApp()){
			rolePermissionGroupRefQueryParam.setGroupAppKey(param.getAppKey());
		}
		List<PermissionGroupPO> permissionGroupPOs = rolePermissionGroupRefDAO.listPermissionGroup(rolePermissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionGroupPOs)){
			List<Long> groupIdsRemove = new ArrayList<Long>();
			for(PermissionGroupPO permissionGroupPO : permissionGroupPOs){
				groupIdsRemove.add(permissionGroupPO.getId());
				rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), groupIdsRemove, IceAclConstant.STATUS_REMOVE);
			}
		}
		List<RolePermissionGroupRefPO> list = new ArrayList<RolePermissionGroupRefPO>();
		Date now = new Date();
		for(Long groupId : groupIds){
			RolePermissionGroupRefPO rolePermissionGroupRefPO = new RolePermissionGroupRefPO();
			rolePermissionGroupRefPO.setGroupId(groupId);
			rolePermissionGroupRefPO.setRoleId(param.getRoleId());
			rolePermissionGroupRefPO.setCreateTime(now);
			rolePermissionGroupRefPO.setUpdateTime(now);
			rolePermissionGroupRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(rolePermissionGroupRefPO);
		}
		rolePermissionGroupRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		checkRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		checkRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		checkRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), IceAclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public List<PermissionGroupDTO> listPermissionGroup(RolePermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(param.getAppKey(), roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<PermissionGroupPO> permissionGroupPOs = rolePermissionGroupRefDAO.listPermissionGroup(param);
		return BizUtils.buildPermissionGroupDTOs(permissionGroupPOs);
	}

	@Override
	public PageVO<PermissionGroupDTO> pagePermissionGroup(RolePermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(param.getAppKey(), roleId);
		Assert.notNull(roleDTO, "角色不存在");
		PageVO<PermissionGroupDTO> pageVO = new PageVO<PermissionGroupDTO>(param);
		if(param.isReturnTotalCount()){
			long total = rolePermissionGroupRefDAO.countPermissionGroup(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<PermissionGroupPO> permissionGroupPOs = rolePermissionGroupRefDAO.pagePermissionGroup(param);
		pageVO.setItems(BizUtils.buildPermissionGroupDTOs(permissionGroupPOs));
		return pageVO;
	}

	@Override
	public List<Long> listUserRolePermissionGroupIds(String userId, String appKey, String tenant) {
		Assert.hasText(userId, "userId不能为空");
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		UserRoleRefQueryParam param = new UserRoleRefQueryParam();
		param.setUserId(userId);
		param.setTenant(tenant);
		param.setAppKey(appKey);
		param.setReturnNotEffective(false);
		List<UserRoleRefDTO> roles = userRoleRefManager.listUserRoleRef(param);
		if(CollectionUtil.isEmpty(roles)){
			return null;
		}
		List<Long> roleIds = Lists.newArrayList();
		for(UserRoleRefDTO role : roles){
			roleIds.add(role.getRoleId());
		}
		//查询角色和权限组关联关系不需要关联租户
		List<PermissionGroupPO> parentpermissionGroupPOs = rolePermissionGroupRefDAO
				.getPermissionGroupsByRoleIds(appKey, roleIds);
		if (CollectionUtil.isEmpty(parentpermissionGroupPOs)) {
			return null;
		}
		
		List<Long> groupIds = Lists.newArrayList();
		for(PermissionGroupPO permissionGroupPO : parentpermissionGroupPOs){
			groupIds.add(permissionGroupPO.getId());
		}
		return groupIds;
		
	}
	
	private void checkRolePermissionGroupRefParam(RolePermissionGroupRefParam param){
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<Long> groupIds = param.getGroupIds();
		if(CollectionUtil.isNotEmpty(groupIds)){
			List<PermissionGroupDTO> permissionGroupDTOs = permissionGroupManager.getPermissionGroupByIds(groupIds);
			Assert.notEmpty(permissionGroupDTOs, "权限组不存在");
			if(param.isMultiApp()){
				for(PermissionGroupDTO permissionGroupDTO : permissionGroupDTOs){
					Assert.isTrue(appKey.equals(permissionGroupDTO.getAppKey()), "appKey权限组不匹配:" + permissionGroupDTO.getId());
				}
			}
			Map<Object, PermissionGroupDTO> permissionGroupMap = CollectionUtil.toIdMap(permissionGroupDTOs);
			for(Long groupId : groupIds){
				Assert.notNull(permissionGroupMap.get(groupId), "权限组不存在:" + groupId);
			}
		}
	}
}
