package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.PermissionDAO;
import com.kingbo401.acl.dao.PermissionGroupRefDAO;
import com.kingbo401.acl.manager.PermissionGroupManager;
import com.kingbo401.acl.manager.PermissionGroupRefManager;
import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.dto.param.PermissionGroupRefParam;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.PermissionGroupRefDO;
import com.kingbo401.acl.model.entity.param.PermissionGroupRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class PermissionGroupRefManagerImpl implements PermissionGroupRefManager{
	@Autowired
	private PermissionGroupRefDAO permissionGroupRefDAO;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
	@Autowired
	private PermissionDAO permissionDAO;
	
	@Override
	public boolean addPermissionGroupRef(PermissionGroupRefParam param) {
		Assert.notNull(param, "参数不能为空");
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		checkPermissionGroupRefParam(param);
		List<PermissionGroupRefDO> list = new ArrayList<PermissionGroupRefDO>();
		for(Long permissionId : permissionIds){
			PermissionGroupRefDO permissionGroupRefDO = new PermissionGroupRefDO();
			permissionGroupRefDO.setPermissionId(permissionId);
			permissionGroupRefDO.setGroupId(param.getGroupId());
			permissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(permissionGroupRefDO);
		}
		permissionGroupRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean updatePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		Long groupId = param.getGroupId();
		PermissionGroupRefQueryParam permissionGroupRefQueryParam = new PermissionGroupRefQueryParam();
		permissionGroupRefQueryParam.setGroupId(groupId);
		List<PermissionDO> permissionDOs =  permissionGroupRefDAO.listPermission(permissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionDOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionDO permissionDO : permissionDOs){
				permissionIdsRemove.add(permissionDO.getId());
			}
			permissionGroupRefDAO.updateRefsStatus(groupId, permissionIdsRemove, AclConstant.STATUS_REMOVE);
		}
		List<PermissionGroupRefDO> list = new ArrayList<PermissionGroupRefDO>();
		for(Long permissionId : permissionIds){
			PermissionGroupRefDO permissionGroupRefDO = new PermissionGroupRefDO();
			permissionGroupRefDO.setPermissionId(permissionId);
			permissionGroupRefDO.setGroupId(param.getGroupId());
			permissionGroupRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(permissionGroupRefDO);
		}
		permissionGroupRefDAO.batchCreate(list);		
		return true;
	}

	@Override
	public boolean removePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), AclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), AclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), AclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public PageVO<PermissionDTO> pagePermission(PermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long groupId = param.getGroupId();
		Assert.notNull(groupId, "groupId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
		Assert.notNull(permissionGroupDTO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupDTO.getAppKey()), "权限组appkey不匹配");
		PageVO<PermissionDTO> pageVO = new PageVO<PermissionDTO>(param);
		if(param.isReturnTotalCount()){
			long total = permissionGroupRefDAO.countPermission(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<PermissionDO> permissionDOs = permissionGroupRefDAO.pagePermission(param);
		pageVO.setItems(BizUtil.buildPermissionDTOs(permissionDOs));
		return pageVO;
	}

	@Override
	public List<PermissionDTO> listPermission(PermissionGroupRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long groupId = param.getGroupId();
		Assert.notNull(groupId, "groupId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
		Assert.notNull(permissionGroupDTO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupDTO.getAppKey()), "权限组appkey不匹配");
		List<PermissionDO> permissionDOs = permissionGroupRefDAO.listPermission(param);
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}

	private void checkPermissionGroupRefParam(PermissionGroupRefParam param){
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long groupId = param.getGroupId();
		Assert.notNull(groupId, "groupId不能为空");
		PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
		Assert.notNull(permissionGroupDTO, "权限组不存在");
		Assert.isTrue(appKey.equals(permissionGroupDTO.getAppKey()), "权限组appKey不匹配");
		List<Long> permissionIds = param.getPermissionIds();
		if(CollectionUtil.isNotEmpty(permissionIds)){
			List<PermissionDO> permissionDOs = permissionDAO.getPermissionByIds(permissionIds);
			Assert.notEmpty(permissionDOs, "权限不存在");
			Map<Long, PermissionDO> permissionMap = permissionDOs.stream().collect(Collectors.toMap(PermissionDO::getId, a -> a, (k1, k2) -> k1));
			for(Long permissionId : permissionIds){
				Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
			}
			for(PermissionDO permissionDO : permissionDOs){
				Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "权限appkey不匹配:" + permissionDO.getId());
			}
		}
	}
}
