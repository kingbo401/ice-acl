package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.model.dto.RoleDTO;
import com.kingbo401.acl.common.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.common.model.dto.UserRoleRefsDTO;
import com.kingbo401.acl.common.model.dto.param.UserRoleRefParam;
import com.kingbo401.acl.dao.UserRoleRefDAO;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.manager.UserRoleRefManager;
import com.kingbo401.acl.model.entity.UserRoleRefDO;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.model.entity.param.UsersRoleRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;

@Service
public class UserRoleRefManagerImpl implements UserRoleRefManager{
	@Autowired
	private UserRoleRefDAO userRoleRefDAO;
	@Autowired
	private RoleManager roleManager;
	
	@Override
	public PageVO<UserRoleRefsDTO> pageUserRoleRefs(UsersRoleRefQueryParam usersRoleRefQueryParam) {
		Assert.notNull(usersRoleRefQueryParam, "参数不能为空");
		String appKey = usersRoleRefQueryParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String tenant = usersRoleRefQueryParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		PageVO<UserRoleRefsDTO> pageVO = new PageVO<UserRoleRefsDTO>(usersRoleRefQueryParam);
		if(usersRoleRefQueryParam.isReturnTotalCount()){
			long total = userRoleRefDAO.countUser(usersRoleRefQueryParam);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<String> userIds = userRoleRefDAO.pageUser(usersRoleRefQueryParam);
		usersRoleRefQueryParam.setUserIds(userIds);
		List<UserRoleRefDTO> userRoleRefVOs = userRoleRefDAO.listUsersRoleRef(usersRoleRefQueryParam);
		if(CollectionUtils.isEmpty(userRoleRefVOs)){
			return pageVO;
		}
		Map<Long, List<UserRoleRefDTO>> userRoleRefVOsMap = new HashMap<>();
		for(UserRoleRefDTO  userRoleRefVO : userRoleRefVOs){
			Long userId = userRoleRefVO.getUserId();
			List<UserRoleRefDTO> refVOs = userRoleRefVOsMap.get(userId);
			if(refVOs == null){
				refVOs = new ArrayList<UserRoleRefDTO>();
				userRoleRefVOsMap.put(userId, refVOs);
			}
			refVOs.add(userRoleRefVO);
		}
		List<UserRoleRefsDTO> datas = new ArrayList<UserRoleRefsDTO>();
		for(Entry<Long, List<UserRoleRefDTO>> entry : userRoleRefVOsMap.entrySet()){
			UserRoleRefsDTO data = new UserRoleRefsDTO();
			data.setUserId(entry.getKey());
			data.setRefs(entry.getValue());
			datas.add(data);
		}
		pageVO.setItems(datas);
		return pageVO;
	}

	@Override
	public List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam userRoleQueryParam) {
		Assert.notNull(userRoleQueryParam, "参数不能为空");
		Assert.hasText(userRoleQueryParam.getAppKey(), "appKey不能为空");
		Assert.hasText(userRoleQueryParam.getTenant(), "tenant不能为空");
		return userRoleRefDAO.listUserRoleRef(userRoleQueryParam);
	}

	@Override
	public PageVO<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam userRoleQueryParam) {
		Assert.notNull(userRoleQueryParam, "参数不能为空");
		Assert.hasText(userRoleQueryParam.getAppKey(), "appKey不能为空");
		Assert.hasText(userRoleQueryParam.getTenant(), "tenant不能为空");
		PageVO<UserRoleRefDTO> pageVO = new PageVO<UserRoleRefDTO>(userRoleQueryParam);
		if(userRoleQueryParam.isReturnTotalCount()){
			long total = userRoleRefDAO.countUserRoleRef(userRoleQueryParam);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<UserRoleRefDTO> items = userRoleRefDAO.pageUserRoleRef(userRoleQueryParam);
		pageVO.setItems(items);
		return pageVO;
	}

	private void assertRoles(String appKey, String roleType, List<Long> roleIds){
		if(CollectionUtil.isEmpty(roleIds)){
			return;
		}
		List<RoleDTO> roleDTOs = roleManager.getByIds(appKey, roleIds);
		Assert.notEmpty(roleDTOs, "角色不存在");
		Map<Long, RoleDTO> roleIdMap = roleDTOs.stream().collect(Collectors.toMap(RoleDTO::getId, a -> a, (k1, k2) -> k1));
		for(Long roleId : roleIds){
			RoleDTO roleDTO = roleIdMap.get(roleId);
			Assert.notNull(roleDTO, "角色:" + roleId + " 不存在");
			if(StringUtils.isNotBlank(roleType)){
				Assert.isTrue(roleType.equals(roleDTO.getSubgroup()), "角色类型不匹配");
			}
		}
	}
	
	@Override
	public boolean addRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		assertRoles(appKey, param.getSubgroup(), roleIds);
		List<UserRoleRefDO> userRoleRefDOs = new ArrayList<UserRoleRefDO>();
		for(Long roleId : roleIds){
			UserRoleRefDO userRoleRefDO = new UserRoleRefDO();
			userRoleRefDO.setUserId(userId);
			userRoleRefDO.setTenant(tenant);
			userRoleRefDO.setRoleId(roleId);
			userRoleRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userRoleRefDO.setEffectiveTime(effectiveTime);
			userRoleRefDO.setExpireTime(expireTime);
			userRoleRefDOs.add(userRoleRefDO);
		}
		userRoleRefDAO.batchCreate(userRoleRefDOs);
		return true;
	}

	@Override
	public boolean updateRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		BizUtil.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		assertRoles(appKey, param.getSubgroup(), roleIds);
		UserRoleRefQueryParam userRoleRefQueryParam = new UserRoleRefQueryParam();
		userRoleRefQueryParam.setAppKey(appKey);
		userRoleRefQueryParam.setReturnNotEffective(true);
		userRoleRefQueryParam.setTenant(tenant);
		userRoleRefQueryParam.setUserId(userId);
		List<UserRoleRefDTO> userRoleRefVOs = userRoleRefDAO.listUserRoleRef(userRoleRefQueryParam);
		if(CollectionUtils.isNotEmpty(userRoleRefVOs)){
			List<Long> removeRoleIds = userRoleRefVOs.stream().map(UserRoleRefDTO::getRoleId).collect(Collectors.toList());
			userRoleRefDAO.updateRefsStatus(userId, tenant, removeRoleIds, AclConstant.STATUS_REMOVE);
		}
		List<UserRoleRefDO> userRoleRefDOs = new ArrayList<UserRoleRefDO>();
		for(Long roleId : roleIds){
			UserRoleRefDO userRoleRefDO = new UserRoleRefDO();
			userRoleRefDO.setUserId(userId);
			userRoleRefDO.setTenant(tenant);
			userRoleRefDO.setRoleId(roleId);
			userRoleRefDO.setStatus(AclConstant.STATUS_NORMAL);
			userRoleRefDO.setEffectiveTime(effectiveTime);
			userRoleRefDO.setExpireTime(expireTime);
			userRoleRefDOs.add(userRoleRefDO);
		}
		userRoleRefDAO.batchCreate(userRoleRefDOs);
		return true;
	}
	
	private boolean updateRefStatus(UserRoleRefParam param, int status) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		assertRoles(appKey, param.getSubgroup(), roleIds);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.notNull(userId, "userId不能为空");
		if (CollectionUtils.isEmpty(roleIds)) {
			UserRoleRefQueryParam userRoleRefQueryParam = new UserRoleRefQueryParam();
			userRoleRefQueryParam.setAppKey(appKey);
			userRoleRefQueryParam.setReturnNotEffective(true);
			userRoleRefQueryParam.setTenant(tenant);
			userRoleRefQueryParam.setUserId(userId);
			List<UserRoleRefDTO> userRoleRefVOs = userRoleRefDAO.listUserRoleRef(userRoleRefQueryParam);
			if(CollectionUtils.isEmpty(userRoleRefVOs)){
				return true;
			}
			roleIds = userRoleRefVOs.stream().map(UserRoleRefDTO::getRoleId).collect(Collectors.toList());
		}
		userRoleRefDAO.updateRefsStatus(userId, tenant, roleIds, status);
		return true;
	}
	
	@Override
	public boolean removeRef(UserRoleRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeRef(UserRoleRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeRef(UserRoleRefParam param) {
		return this.updateRefStatus(param, AclConstant.STATUS_NORMAL);
	}

	@Override
	public List<String> listUserTenant(String userId, String appKey, List<String> roleKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(userId, "userId不能为空");
		return userRoleRefDAO.listUserTenant(userId, appKey, roleKeys);
	}

	@Override
	public boolean hasUserUse(long roleId) {
		return userRoleRefDAO.hasUserUse(roleId) != null;
	}
}
