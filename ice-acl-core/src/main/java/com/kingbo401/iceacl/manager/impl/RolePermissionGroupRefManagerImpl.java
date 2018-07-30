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
import com.kingbo401.iceacl.model.db.PermissionGroupDO;
import com.kingbo401.iceacl.model.db.RolePermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.RolePermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.db.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.param.RolePermissionGroupRefParam;
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
		List<RolePermissionGroupRefDO> list = new ArrayList<RolePermissionGroupRefDO>();
		Date now = new Date();
		for(Long groupId : groupIds){
			RolePermissionGroupRefDO rolePermissionGroupRefDO = new RolePermissionGroupRefDO();
			rolePermissionGroupRefDO.setGroupId(groupId);
			rolePermissionGroupRefDO.setRoleId(param.getRoleId());
			rolePermissionGroupRefDO.setCreateTime(now);
			rolePermissionGroupRefDO.setUpdateTime(now);
			rolePermissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
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
		checkRolePermissionGroupRefParam(param);
		
		RolePermissionGroupRefQueryParam rolePermissionGroupRefQueryParam = new RolePermissionGroupRefQueryParam();
		rolePermissionGroupRefQueryParam.setRoleId(param.getRoleId());
		if(!param.isMultiApp()){
			rolePermissionGroupRefQueryParam.setGroupAppKey(param.getAppKey());
		}
		List<PermissionGroupDO> permissionGroupDOs = rolePermissionGroupRefDAO.listPermissionGroup(rolePermissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionGroupDOs)){
			List<Long> groupIdsRemove = new ArrayList<Long>();
			for(PermissionGroupDO permissionGroupDO : permissionGroupDOs){
				groupIdsRemove.add(permissionGroupDO.getId());
				rolePermissionGroupRefDAO.updateRefsStatus(param.getRoleId(), groupIdsRemove, IceAclConstant.STATUS_REMOVE);
			}
		}
		List<RolePermissionGroupRefDO> list = new ArrayList<RolePermissionGroupRefDO>();
		Date now = new Date();
		for(Long groupId : groupIds){
			RolePermissionGroupRefDO rolePermissionGroupRefDO = new RolePermissionGroupRefDO();
			rolePermissionGroupRefDO.setGroupId(groupId);
			rolePermissionGroupRefDO.setRoleId(param.getRoleId());
			rolePermissionGroupRefDO.setCreateTime(now);
			rolePermissionGroupRefDO.setUpdateTime(now);
			rolePermissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(rolePermissionGroupRefDO);
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
		List<PermissionGroupDO> permissionGroupDOs = rolePermissionGroupRefDAO.listPermissionGroup(param);
		return BizUtils.buildPermissionGroupDTOs(permissionGroupDOs);
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
		pageVO.setItems(BizUtils.buildPermissionGroupDTOs(permissionGroupDOs));
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
		List<UserRoleRefVO> roles = userRoleRefManager.listUserRoleRef(param);
		if(CollectionUtil.isEmpty(roles)){
			return null;
		}
		List<Long> roleIds = Lists.newArrayList();
		for(UserRoleRefVO role : roles){
			roleIds.add(role.getRoleId());
		}
		//查询角色和权限组关联关系不需要关联租户
		List<PermissionGroupDO> parentPermissionGroupDOs = rolePermissionGroupRefDAO
				.getPermissionGroupsByRoleIds(appKey, roleIds);
		if (CollectionUtil.isEmpty(parentPermissionGroupDOs)) {
			return null;
		}
		
		List<Long> groupIds = Lists.newArrayList();
		for(PermissionGroupDO permissionGroupDO : parentPermissionGroupDOs){
			groupIds.add(permissionGroupDO.getId());
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
