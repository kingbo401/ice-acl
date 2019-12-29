package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.kingbo401.acl.dao.RolePermissionGroupRefDAO;
import com.kingbo401.acl.manager.PermissionGroupManager;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.manager.RolePermissionGroupRefManager;
import com.kingbo401.acl.manager.UserRoleRefManager;
import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.dto.RoleDTO;
import com.kingbo401.acl.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionGroupRefParam;
import com.kingbo401.acl.model.entity.PermissionGroupDO;
import com.kingbo401.acl.model.entity.RolePermissionGroupRefDO;
import com.kingbo401.acl.model.entity.param.RolePermissionGroupRefQueryParam;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

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
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		assertRolePermissionGroupRefParam(param);
		List<RolePermissionGroupRefDO> list = new ArrayList<RolePermissionGroupRefDO>();
		for(Long groupId : groupIds){
			RolePermissionGroupRefDO rolePermissionGroupRefDO = new RolePermissionGroupRefDO();
			rolePermissionGroupRefDO.setGroupId(groupId);
			rolePermissionGroupRefDO.setRoleId(param.getRoleId());
			rolePermissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(rolePermissionGroupRefDO);
		}
		rolePermissionGroupRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean updateRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		assertRolePermissionGroupRefParam(param);
		
		RolePermissionGroupRefQueryParam rolePermissionGroupRefQueryParam = new RolePermissionGroupRefQueryParam();
		rolePermissionGroupRefQueryParam.setRoleId(param.getRoleId());
		rolePermissionGroupRefQueryParam.setSubgroup(param.getSubgroup());
		List<PermissionGroupDO> permissionGroupDOs = rolePermissionGroupRefDAO.listPermissionGroup(rolePermissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionGroupDOs)){
			List<Long> groupIdsRemove = new ArrayList<Long>();
			for(PermissionGroupDO permissionGroupDO : permissionGroupDOs){
				groupIdsRemove.add(permissionGroupDO.getId());
				rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), groupIdsRemove, AclConstant.STATUS_REMOVE);
			}
		}
		List<RolePermissionGroupRefDO> list = new ArrayList<RolePermissionGroupRefDO>();
		for(Long groupId : groupIds){
			RolePermissionGroupRefDO rolePermissionGroupRefDO = new RolePermissionGroupRefDO();
			rolePermissionGroupRefDO.setGroupId(groupId);
			rolePermissionGroupRefDO.setRoleId(param.getRoleId());
			rolePermissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(rolePermissionGroupRefDO);
		}
		rolePermissionGroupRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		assertRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), AclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		assertRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), AclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeRolePermissionGroupRef(RolePermissionGroupRefParam param) {
		assertRolePermissionGroupRefParam(param);
		rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), param.getGroupIds(), AclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public List<PermissionGroupDTO> listPermissionGroup(RolePermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(param.getAppKey(), roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<PermissionGroupDO> permissionGroupDOs = rolePermissionGroupRefDAO.listPermissionGroup(param);
		return BizUtil.buildPermissionGroupDTOs(permissionGroupDOs);
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
		List<PermissionGroupDO> permissionGroupDOs = rolePermissionGroupRefDAO.pagePermissionGroup(param);
		pageVO.setItems(BizUtil.buildPermissionGroupDTOs(permissionGroupDOs));
		return pageVO;
	}

	@Override
	public List<Long> listUserRolePermissionGroupIds(String userId, String appKey, String tenant) {
		Assert.notNull(userId, "userId不能为空");
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
		List<PermissionGroupDO> parentpermissionGroupDOs = rolePermissionGroupRefDAO
				.getPermissionGroupsByRoleIds(roleIds);
		if (CollectionUtil.isEmpty(parentpermissionGroupDOs)) {
			return null;
		}
		
		List<Long> groupIds = Lists.newArrayList();
		for(PermissionGroupDO permissionGroupDO : parentpermissionGroupDOs){
			groupIds.add(permissionGroupDO.getId());
		}
		return groupIds;
		
	}
	
	private void assertRolePermissionGroupRefParam(RolePermissionGroupRefParam param){
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<Long> groupIds = param.getGroupIds();
		String subgroup = param.getSubgroup();
		if(CollectionUtil.isNotEmpty(groupIds)){
			List<PermissionGroupDTO> permissionGroupDTOs = permissionGroupManager.getPermissionGroupByIds(groupIds);
			Assert.notEmpty(permissionGroupDTOs, "权限组不存在");
			Map<Long, PermissionGroupDTO> permissionGroupMap = permissionGroupDTOs.stream().collect(Collectors.toMap(PermissionGroupDTO::getId, a -> a, (k1, k2) -> k1));
			for(Long groupId : groupIds){
				PermissionGroupDTO permissionGroupDTO = permissionGroupMap.get(groupId);
				Assert.notNull(permissionGroupDTO, "权限组不存在:" + groupId);
				if (StringUtils.isNotBlank(subgroup)) {
					Assert.isTrue(subgroup.equals(permissionGroupDTO.getSubgroup()), "subgrou非法");
				}
			}
		}
	}
}
