package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.PermissionDAO;
import com.kingbo401.iceacl.dao.PermissionGroupRefDAO;
import com.kingbo401.iceacl.manager.PermissionGroupManager;
import com.kingbo401.iceacl.manager.PermissionGroupRefManager;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.PermissionGroupRefDO;
import com.kingbo401.iceacl.model.db.param.PermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.param.PermissionGroupRefParam;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;

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
		Date now = new Date();
		for(Long permissionId : permissionIds){
			PermissionGroupRefDO permissionGroupRefDO = new PermissionGroupRefDO();
			permissionGroupRefDO.setPermissionId(permissionId);
			permissionGroupRefDO.setGroupId(param.getGroupId());
			permissionGroupRefDO.setCreateTime(now);
			permissionGroupRefDO.setUpdateTime(now);
			permissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
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
		String appKey = param.getAppKey();
		PermissionGroupRefQueryParam permissionGroupRefQueryParam = new PermissionGroupRefQueryParam();
		permissionGroupRefQueryParam.setGroupId(groupId);
		if(!param.isMultiApp()){
			permissionGroupRefQueryParam.setPermissionAppKey(appKey);
		}
		List<PermissionDO> permissionDOs =  permissionGroupRefDAO.listPermission(permissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionDOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionDO permissionDO : permissionDOs){
				permissionIdsRemove.add(permissionDO.getId());
			}
			permissionGroupRefDAO.updateRefsStatus(groupId, permissionIdsRemove, IceAclConstant.STATUS_REMOVE);
		}
		List<PermissionGroupRefDO> list = new ArrayList<PermissionGroupRefDO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			PermissionGroupRefDO permissionGroupRefDO = new PermissionGroupRefDO();
			permissionGroupRefDO.setPermissionId(permissionId);
			permissionGroupRefDO.setGroupId(param.getGroupId());
			permissionGroupRefDO.setCreateTime(now);
			permissionGroupRefDO.setUpdateTime(now);
			permissionGroupRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(permissionGroupRefDO);
		}
		permissionGroupRefDAO.batchCreate(list);		
		return true;
	}

	@Override
	public boolean removePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezePermissionGroupRef(PermissionGroupRefParam param) {
		checkPermissionGroupRefParam(param);
		permissionGroupRefDAO.updateRefsStatus(param.getGroupId(), param.getPermissionIds(), IceAclConstant.STATUS_NORMAL);
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
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionDOs));
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
		return BizUtils.buildPermissionDTOs(permissionDOs);
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
			Map<Object, PermissionDO> permissionMap = CollectionUtil.toIdMap(permissionDOs);
			for(Long permissionId : permissionIds){
				Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
			}
			if(!param.isMultiApp()){
				for(PermissionDO permissionDO : permissionDOs){
					Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "权限appkey不匹配:" + permissionDO.getId());
				}
			}
		}
	}
}
