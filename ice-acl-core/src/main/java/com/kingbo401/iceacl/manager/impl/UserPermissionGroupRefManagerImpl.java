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
import com.kingbo401.iceacl.model.db.UserPermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserPermissionGroupRefVO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionGroupRefParam;

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
		List<UserPermissionGroupRefDO> userPermissionGroupRefDOs = new ArrayList<UserPermissionGroupRefDO>();
		Date now  = new Date();
		for(Long groupId : groupIds){
			UserPermissionGroupRefDO userPermissionGroupRefDO = new UserPermissionGroupRefDO();
			userPermissionGroupRefDO.setUserId(userId);
			userPermissionGroupRefDO.setTenant(tenant);
			userPermissionGroupRefDO.setGroupId(groupId);
			userPermissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			userPermissionGroupRefDO.setCreateTime(now);
			userPermissionGroupRefDO.setUpdateTime(now);
			userPermissionGroupRefDO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefDO.setExpireTime(expireTime);
			userPermissionGroupRefDOs.add(userPermissionGroupRefDO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefDOs);
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
		List<UserPermissionGroupRefVO> userPermissionGroupRefVOs = userPermissionGroupRefDAO.listUserPermissionGroupRef(userPermissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(userPermissionGroupRefVOs)){
			List<Long> groupIdsRemove = new ArrayList<Long>();
			for(UserPermissionGroupRefVO userPermissionGroupRefVO : userPermissionGroupRefVOs){
				groupIdsRemove.add(userPermissionGroupRefVO.getGroupId());
			}
			userPermissionGroupRefDAO.updateRefsStats(userId, tenant, groupIdsRemove, IceAclConstant.STATUS_REMOVE);
		}
		
		List<UserPermissionGroupRefDO> userPermissionGroupRefDOs = new ArrayList<UserPermissionGroupRefDO>();
		Date now  = new Date();
		for(Long groupId : groupIds){
			UserPermissionGroupRefDO userPermissionGroupRefDO = new UserPermissionGroupRefDO();
			userPermissionGroupRefDO.setUserId(userId);
			userPermissionGroupRefDO.setTenant(tenant);
			userPermissionGroupRefDO.setGroupId(groupId);
			userPermissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			userPermissionGroupRefDO.setCreateTime(now);
			userPermissionGroupRefDO.setUpdateTime(now);
			userPermissionGroupRefDO.setEffectiveTime(effectiveTime);
			userPermissionGroupRefDO.setExpireTime(expireTime);
			userPermissionGroupRefDOs.add(userPermissionGroupRefDO);
		}
		userPermissionGroupRefDAO.batchCreate(userPermissionGroupRefDOs);
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
		List<UserPermissionGroupRefVO> parentPermissionGroupVOs = userPermissionGroupRefDAO.listUserPermissionGroupRef(param);
		if(CollectionUtil.isEmpty(parentPermissionGroupVOs)){
			return null;
		}
		
		List<Long> groupIds = Lists.newArrayList();
		for(UserPermissionGroupRefVO userPermissionGroupRefVO : parentPermissionGroupVOs){
			groupIds.add(userPermissionGroupRefVO.getGroupId());
		}
		return groupIds;
	}
	
	@Override
	public PageVO<UserPermissionGroupRefVO> pageUserPermissionGroup(UserPermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		PageVO<UserPermissionGroupRefVO> pageVO = new PageVO<UserPermissionGroupRefVO>(param);
		if(param.isReturnTotalCount()){
			long total = userPermissionGroupRefDAO.countUserPermissionGroupRef(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<UserPermissionGroupRefVO> userPermissionGroupRefVOs = userPermissionGroupRefDAO.pageUserPermissionGroupRef(param);
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
