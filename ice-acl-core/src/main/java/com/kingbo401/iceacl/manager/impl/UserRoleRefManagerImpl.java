package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.commons.util.StringUtil;
import com.kingbo401.iceacl.dao.UserRoleRefDAO;
import com.kingbo401.iceacl.manager.RoleManager;
import com.kingbo401.iceacl.manager.UserRoleRefManager;
import com.kingbo401.iceacl.model.db.UserRoleRefDO;
import com.kingbo401.iceacl.model.db.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.param.UsersRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.UserRoleRefsDTO;
import com.kingbo401.iceacl.model.dto.param.UserRoleRefParam;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.utils.MixAll;

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
		List<UserRoleRefVO> userRoleRefVOs = userRoleRefDAO.listUsersRoleRef(usersRoleRefQueryParam);
		if(CollectionUtils.isEmpty(userRoleRefVOs)){
			return pageVO;
		}
		Map<String, List<UserRoleRefVO>> userRoleRefVOsMap = new HashMap<String, List<UserRoleRefVO>>();
		for(UserRoleRefVO  userRoleRefVO : userRoleRefVOs){
			String userId = userRoleRefVO.getUserId();
			List<UserRoleRefVO> refVOs = userRoleRefVOsMap.get(userId);
			if(refVOs == null){
				refVOs = new ArrayList<UserRoleRefVO>();
				userRoleRefVOsMap.put(userId, refVOs);
			}
			refVOs.add(userRoleRefVO);
		}
		List<UserRoleRefsDTO> datas = new ArrayList<UserRoleRefsDTO>();
		for(Entry<String, List<UserRoleRefVO>> entry : userRoleRefVOsMap.entrySet()){
			UserRoleRefsDTO data = new UserRoleRefsDTO();
			data.setUserId(entry.getKey());
			data.setRefVOs(entry.getValue());
			datas.add(data);
		}
		pageVO.setItems(datas);
		return pageVO;
	}

	@Override
	public List<UserRoleRefVO> listUserRoleRef(UserRoleRefQueryParam userRoleQueryParam) {
		Assert.notNull(userRoleQueryParam, "参数不能为空");
		Assert.hasText(userRoleQueryParam.getAppKey(), "appKey不能为空");
		Assert.hasText(userRoleQueryParam.getTenant(), "tenant不能为空");
		return userRoleRefDAO.listUserRoleRef(userRoleQueryParam);
	}

	@Override
	public PageVO<UserRoleRefVO> pageUserRoleRef(UserRoleRefQueryParam userRoleQueryParam) {
		Assert.notNull(userRoleQueryParam, "参数不能为空");
		Assert.hasText(userRoleQueryParam.getAppKey(), "appKey不能为空");
		Assert.hasText(userRoleQueryParam.getTenant(), "tenant不能为空");
		PageVO<UserRoleRefVO> pageVO = new PageVO<UserRoleRefVO>(userRoleQueryParam);
		if(userRoleQueryParam.isReturnTotalCount()){
			long total = userRoleRefDAO.countUserRoleRef(userRoleQueryParam);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<UserRoleRefVO> items = userRoleRefDAO.pageUserRoleRef(userRoleQueryParam);
		pageVO.setItems(items);
		return pageVO;
	}

	private void assertRoles(String appKey, String roleType, List<Long> roleIds){
		if(CollectionUtil.isEmpty(roleIds)){
			return;
		}
		List<RoleDTO> roleDTOs = roleManager.getRoleByIds(appKey, roleIds);
		Map<Object, RoleDTO> roleIdMap =  CollectionUtil.toIdMap(roleDTOs);
		for(Long roleId : roleIds){
			RoleDTO roleDTO = roleIdMap.get(roleId);
			Assert.notNull(roleDTO, "角色:" + roleId + " 不存在");
			if(StringUtil.isNotBlank(roleType)){
				Assert.isTrue(roleType.equals(roleDTO.getRoleType()), "角色类型不匹配");
			}
		}
	}
	
	@Override
	public boolean addUserRoleRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		MixAll.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		assertRoles(appKey, param.getRoleType(), roleIds);
		List<UserRoleRefDO> userRoleRefDOs = new ArrayList<UserRoleRefDO>();
		Date now  = new Date();
		for(Long roleId : roleIds){
			UserRoleRefDO userRoleRefDO = new UserRoleRefDO();
			userRoleRefDO.setUserId(userId);
			userRoleRefDO.setTenant(tenant);
			userRoleRefDO.setRoleId(roleId);
			userRoleRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			userRoleRefDO.setCreateTime(now);
			userRoleRefDO.setUpdateTime(now);
			userRoleRefDO.setEffectiveTime(effectiveTime);
			userRoleRefDO.setExpireTime(expireTime);
			userRoleRefDOs.add(userRoleRefDO);
		}
		userRoleRefDAO.batchCreate(userRoleRefDOs);
		return true;
	}

	@Override
	public boolean updateUserRoleRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		Date effectiveTime = param.getEffectiveTime();
		Date expireTime = param.getExpireTime();
		MixAll.checkEffectiveExpireTime(effectiveTime, expireTime);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		assertRoles(appKey, param.getRoleType(), roleIds);
		UserRoleRefQueryParam userRoleRefQueryParam = new UserRoleRefQueryParam();
		userRoleRefQueryParam.setAppKey(appKey);
		userRoleRefQueryParam.setReturnNotEffective(true);
		userRoleRefQueryParam.setTenant(tenant);
		userRoleRefQueryParam.setUserId(userId);
		List<UserRoleRefVO> userRoleRefVOs = userRoleRefDAO.listUserRoleRef(userRoleRefQueryParam);
		if(!CollectionUtils.isEmpty(userRoleRefVOs)){
			List<Long> removeRoleIds = new ArrayList<Long>();
			for(UserRoleRefVO userRoleRefVO : userRoleRefVOs){
				removeRoleIds.add(userRoleRefVO.getRoleId());
			}
			userRoleRefDAO.updateRefsStatus(userId, tenant, removeRoleIds, IceAclConstant.STATUS_REMOVE);
		}
		List<UserRoleRefDO> userRoleRefDOs = new ArrayList<UserRoleRefDO>();
		Date now  = new Date();
		for(Long roleId : roleIds){
			UserRoleRefDO userRoleRefDO = new UserRoleRefDO();
			userRoleRefDO.setUserId(userId);
			userRoleRefDO.setTenant(tenant);
			userRoleRefDO.setRoleId(roleId);
			userRoleRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			userRoleRefDO.setCreateTime(now);
			userRoleRefDO.setUpdateTime(now);
			userRoleRefDO.setEffectiveTime(effectiveTime);
			userRoleRefDO.setExpireTime(expireTime);
			userRoleRefDOs.add(userRoleRefDO);
		}
		userRoleRefDAO.batchCreate(userRoleRefDOs);
		return true;
	}

	@Override
	public boolean removeUserRoleRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		assertRoles(appKey, param.getRoleType(), roleIds);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		userRoleRefDAO.updateRefsStatus(userId, tenant, roleIds, IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeUserRoleRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		assertRoles(appKey, param.getRoleType(), roleIds);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		userRoleRefDAO.updateRefsStatus(userId, tenant, roleIds, IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeUserRoleRef(UserRoleRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		String tenant = param.getTenant();
		String userId = param.getUserId();
		List<Long> roleIds = param.getRoleIds();
		assertRoles(appKey, param.getRoleType(), roleIds);
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(tenant, "tenant不能为空");
		Assert.hasText(userId, "userId不能为空");
		userRoleRefDAO.updateRefsStatus(userId, tenant, roleIds, IceAclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public List<String> listUserTenant(String userId, String appKey, List<String> roleKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(userId, "userId不能为空");
		return userRoleRefDAO.listUserTenant(userId, appKey, roleKeys);
	}

	@Override
	public boolean hasUserUse(long roleId) {
		return userRoleRefDAO.hasUserUse(roleId) != null;
	}
}
