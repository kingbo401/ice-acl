package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.utils.MixAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.util.StringUtil;
import com.kingbo401.iceacl.dao.UserPermissionGroupRefDAO;
import com.kingbo401.iceacl.manager.PermissionGroupManager;
import com.kingbo401.iceacl.manager.UserPermissionGroupRefManager;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionGroupRefParam;
import com.kingbo401.iceacl.model.po.UserPermissionGroupRefPO;
import com.kingbo401.iceacl.model.po.param.UserPermissionGroupRefQueryParam;

@Service
public class UserPermissionGroupRefManagerImpl implements UserPermissionGroupRefManager{
	@Autowired
	private UserPermissionGroupRefDAO userPermissionGroupRefDAO;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
	
	@Override
	public boolean addUserPermissionGroupRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> groupIds = param.getGroupIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		MixAll.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		Assert.notEmpty(groupIds, "groupIds不能为空");
		assertPermissionGroup(appKey, param.getGroupType(), groupIds);
		List<UserPermissionGroupRefPO> userPermissionGroupRefPOs = new ArrayList<UserPermissionGroupRefPO>();
		Date now  = new Date();
		for(Long groupId : groupIds){
			UserPermissionGroupRefPO userPermissionGroupRefPO = new UserPermissionGroupRefPO();
			userPermissionGroupRefPO.setUserId(userId);
			userPermissionGroupRefPO.setTenant(tenant);
			userPermissionGroupRefPO.setGroupId(groupId);
			userPermissionGroupRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			userPermissionGroupRefPO.setCreateTime(now);
			userPermissionGroupRefPO.setUpdateTime(now);
			userPermissionGroupRefPO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefPO.setExpireTime(expireTime);
			userPermissionGroupRefPOs.add(userPermissionGroupRefPO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefPOs);
		return true;
	}

	@Override
	public boolean updateUserPermissionGroupRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> groupIds = param.getGroupIds();
		Assert.notEmpty(groupIds, "groupIds不能为空");
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		MixAll.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		assertPermissionGroup(appKey, param.getGroupType(), groupIds);
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
			userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIdsRemove, IceAclConstant.STATUS_REMOVE);
		}
		
		List<UserPermissionGroupRefPO> userPermissionGroupRefPOs = new ArrayList<UserPermissionGroupRefPO>();
		Date now  = new Date();
		for(Long groupId : groupIds){
			UserPermissionGroupRefPO userPermissionGroupRefPO = new UserPermissionGroupRefPO();
			userPermissionGroupRefPO.setUserId(userId);
			userPermissionGroupRefPO.setTenant(tenant);
			userPermissionGroupRefPO.setGroupId(groupId);
			userPermissionGroupRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			userPermissionGroupRefPO.setCreateTime(now);
			userPermissionGroupRefPO.setUpdateTime(now);
			userPermissionGroupRefPO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefPO.setExpireTime(expireTime);
			userPermissionGroupRefPOs.add(userPermissionGroupRefPO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefPOs);
		return true;
	}

	@Override
	public boolean removeUserPermissionGroupRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getGroupType(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeUserPermissionGroupRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getGroupType(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeUserPermissionGroupRef(UserPermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		List<Long> groupIds = param.getGroupIds();
		assertPermissionGroup(appKey, param.getGroupType(), groupIds);
		userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIds, IceAclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public boolean removeRefsByGroupId(String appKey, long groupId) {
		Assert.hasText(appKey, "appKey不能为空");
		PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
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
	
	private void assertPermissionGroup(String appKey, String groupType, List<Long> groupIds){
		if(CollectionUtil.isEmpty(groupIds)){
			return;
		}
		List<PermissionGroupDTO> permissionGroupDTOs = permissionGroupManager.getPermissionGroupByIds(appKey, groupIds);
		Assert.notEmpty(permissionGroupDTOs, "权限组不存在");
		Map<Object, PermissionGroupDTO> permissionGroupMap = CollectionUtil.toIdMap(permissionGroupDTOs);
		for(Long groupId : groupIds){
			PermissionGroupDTO permissionGroup = permissionGroupMap.get(groupId);
			Assert.notNull(permissionGroup, "权限组不存在:" + groupId);
			if(StringUtil.isNotBlank(groupType)){
				Assert.isTrue(groupType.equals(permissionGroup.getGroupType()), "权限组类型不匹配");
			}
		}
	}

	@Override
	public boolean hasUserUse(long groupId) {
		return userPermissionGroupRefDAO.hasUserUse(groupId) != null;
	}
}
