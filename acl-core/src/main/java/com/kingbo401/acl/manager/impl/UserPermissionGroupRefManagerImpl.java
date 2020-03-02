package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.common.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.acl.common.model.dto.param.UserPermissionGroupRefParam;
import com.kingbo401.acl.dao.UserPermissionGroupRefDAO;
import com.kingbo401.acl.manager.PermissionGroupManager;
import com.kingbo401.acl.manager.UserPermissionGroupRefManager;
import com.kingbo401.acl.model.entity.UserPermissionGroupRefDO;
import com.kingbo401.acl.model.entity.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.util.StringUtil;

@Service
public class UserPermissionGroupRefManagerImpl implements UserPermissionGroupRefManager{
	@Autowired
	private UserPermissionGroupRefDAO userPermissionGroupRefDAO;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
	
	@Override
	public boolean addRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> groupIds = param.getGroupIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		Assert.notEmpty(groupIds, "groupIds不能为空");
		assertPermissionGroup(appKey, param.getSubgroup(), groupIds);
		List<UserPermissionGroupRefDO> userPermissionGroupRefDOs = new ArrayList<UserPermissionGroupRefDO>();
		for(Long groupId : groupIds){
			UserPermissionGroupRefDO userPermissionGroupRefDO = new UserPermissionGroupRefDO();
			userPermissionGroupRefDO.setUserId(userId);
			userPermissionGroupRefDO.setTenant(tenant);
			userPermissionGroupRefDO.setGroupId(groupId);
			userPermissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userPermissionGroupRefDO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefDO.setExpireTime(expireTime);
			userPermissionGroupRefDOs.add(userPermissionGroupRefDO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefDOs);
		return true;
	}

	@Override
	public boolean updateRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		assertPermissionGroup(appKey, param.getSubgroup(), groupIds);
		UserPermissionGroupRefQueryParam userPermissionGroupRefQueryParam = new UserPermissionGroupRefQueryParam();
		userPermissionGroupRefQueryParam.setAppKey(appKey);
		userPermissionGroupRefQueryParam.setUserId(userId);
		userPermissionGroupRefQueryParam.setTenant(tenant);
		List<UserPermissionGroupRefDTO> userPermissionGroupRefVOs = userPermissionGroupRefDAO.listUserPermissionGroupRef(userPermissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(userPermissionGroupRefVOs)){
			List<Long> groupIdsRemove = new ArrayList<Long>();
			for(UserPermissionGroupRefDTO userPermissionGroupRefVO : userPermissionGroupRefVOs){
				groupIdsRemove.add(userPermissionGroupRefVO.getGroupId());
			}
			userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIdsRemove, AclConstant.STATUS_REMOVE);
		}
		
		List<UserPermissionGroupRefDO> userPermissionGroupRefDOs = new ArrayList<UserPermissionGroupRefDO>();
		for(Long groupId : groupIds){
			UserPermissionGroupRefDO userPermissionGroupRefDO = new UserPermissionGroupRefDO();
			userPermissionGroupRefDO.setUserId(userId);
			userPermissionGroupRefDO.setTenant(tenant);
			userPermissionGroupRefDO.setGroupId(groupId);
			userPermissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userPermissionGroupRefDO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefDO.setExpireTime(expireTime);
			userPermissionGroupRefDOs.add(userPermissionGroupRefDO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefDOs);
		return true;
	}

	@Override
	public boolean removeRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getSubgroup(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, AclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getSubgroup(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, AclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getSubgroup(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, AclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public boolean removeRefsByGroupId(String appKey, long groupId) {
		Assert.hasText(appKey, "appKey不能为空");
		PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getById(groupId);
		Assert.notNull(permissionGroupDTO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupDTO.getAppKey()), "appKey权限组不匹配");
		userPermissionGroupRefDAO.removeRefsByGroupId(groupId);
		return true;
	}

	@Override
	public List<Long> listUserPermissionGroupIds(UserPermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		List<UserPermissionGroupRefDTO> parentPermissionGroupVOs = userPermissionGroupRefDAO.listUserPermissionGroupRef(param);
		if(CollectionUtil.isEmpty(parentPermissionGroupVOs)){
			return null;
		}
		
		List<Long> groupIds = Lists.newArrayList();
		for(UserPermissionGroupRefDTO userPermissionGroupRefVO : parentPermissionGroupVOs){
			groupIds.add(userPermissionGroupRefVO.getGroupId());
		}
		return groupIds;
	}
	
	@Override
	public PageVO<UserPermissionGroupRefDTO> pageUserPermissionGroup(UserPermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		PageVO<UserPermissionGroupRefDTO> pageVO = new PageVO<UserPermissionGroupRefDTO>(param);
		if(param.isReturnTotalCount()){
			long total = userPermissionGroupRefDAO.countUserPermissionGroupRef(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<UserPermissionGroupRefDTO> userPermissionGroupRefVOs = userPermissionGroupRefDAO.pageUserPermissionGroupRef(param);
		pageVO.setItems(userPermissionGroupRefVOs);
		return pageVO;
	}
	
	private void assertPermissionGroup(String appKey, String subgroup, List<Long> groupIds){
		if(CollectionUtil.isEmpty(groupIds)){
			return;
		}
		List<PermissionGroupDTO> permissionGroupDTOs = permissionGroupManager.getByIds(appKey, groupIds);
		Assert.notEmpty(permissionGroupDTOs, "权限组不存在");
		Map<Long, PermissionGroupDTO> permissionGroupMap = permissionGroupDTOs.stream().collect(Collectors.toMap(PermissionGroupDTO::getId, a -> a, (k1, k2) -> k1));
		for(Long groupId : groupIds){
			PermissionGroupDTO permissionGroup = permissionGroupMap.get(groupId);
			Assert.notNull(permissionGroup, "权限组不存在:" + groupId);
			if(StringUtil.isNotBlank(subgroup)){
				Assert.isTrue(subgroup.equals(permissionGroup.getSubgroup()), "权限组类型不匹配");
			}
		}
	}

	@Override
	public boolean hasUserUse(long groupId) {
		return userPermissionGroupRefDAO.hasUserUse(groupId) != null;
	}
}
