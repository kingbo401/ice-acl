package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.RolePermissionRefDAO;
import com.kingbo401.iceacl.manager.PermissionManager;
import com.kingbo401.iceacl.manager.RoleManager;
import com.kingbo401.iceacl.manager.RolePermissionRefManager;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.RolePermissionRefDO;
import com.kingbo401.iceacl.model.db.param.RolePermissionRefQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.param.RolePermissionIdRefParam;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;

@Service
public class RolePermissionRefManagerImpl implements RolePermissionRefManager{
	@Autowired
	private RolePermissionRefDAO rolePermissionRefDAO;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private PermissionManager permissionManager;
	
	@Override
	public boolean addRolePermissionRef(RolePermissionIdRefParam param) {
		Assert.notNull(param, "参数不能为空");
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		checkRolePermissionIdRefParam(param);
		List<RolePermissionRefDO> list = new ArrayList<RolePermissionRefDO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			RolePermissionRefDO rolePermissionRefDO = new RolePermissionRefDO();
			rolePermissionRefDO.setPermissionId(permissionId);
			rolePermissionRefDO.setRoleId(param.getRoleId());
			rolePermissionRefDO.setCreateTime(now);
			rolePermissionRefDO.setUpdateTime(now);
			rolePermissionRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(rolePermissionRefDO);
		}
		rolePermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean updateRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		List<Long> permissionIds = param.getPermissionIds();
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		Long roleId = param.getRoleId();
		String appKey = param.getAppKey();
		RolePermissionRefQueryParam permissionGroupRefQueryParam = new RolePermissionRefQueryParam();
		permissionGroupRefQueryParam.setRoleId(roleId);
		if(!param.isMultiApp()){
			permissionGroupRefQueryParam.setPermissionAppKey(appKey);
		}
		List<PermissionDO> permissionDOs =  rolePermissionRefDAO.listPermission(permissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionDOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionDO permissionDO : permissionDOs){
				permissionIdsRemove.add(permissionDO.getId());
			}
			rolePermissionRefDAO.updateRefsStatus(roleId, permissionIdsRemove, IceAclConstant.STATUS_REMOVE);
		}
		
		List<RolePermissionRefDO> list = new ArrayList<RolePermissionRefDO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			RolePermissionRefDO rolePermissionRefDO = new RolePermissionRefDO();
			rolePermissionRefDO.setPermissionId(permissionId);
			rolePermissionRefDO.setRoleId(roleId);
			rolePermissionRefDO.setCreateTime(now);
			rolePermissionRefDO.setUpdateTime(now);
			rolePermissionRefDO.setStatus(IceAclConstant.STATUS_NORMAL);
			list.add(rolePermissionRefDO);
		}
		rolePermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), IceAclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), IceAclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), IceAclConstant.STATUS_NORMAL);
		return true;
	}

	@Override
	public List<PermissionDTO> listRolePermission(RolePermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<PermissionDO> permissionDOs = rolePermissionRefDAO.listPermission(param);
		return BizUtils.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public PageVO<PermissionDTO> pageRolePermission(RolePermissionRefQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		PageVO<PermissionDTO> pageVO = new PageVO<PermissionDTO>(param);
		if(param.isReturnTotalCount()){
			long total = rolePermissionRefDAO.countPermission(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<PermissionDO> permissionDOs = rolePermissionRefDAO.pagePermission(param);
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionDOs));
		return pageVO;
	}

	private void checkRolePermissionIdRefParam(RolePermissionIdRefParam param){
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为空");
		RoleDTO roleDTO = roleManager.getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		List<Long> permissionIds = param.getPermissionIds();
		if(CollectionUtil.isNotEmpty(permissionIds)){
			List<PermissionDTO> permissionDTOs = permissionManager.getPermissionByIds(permissionIds);
			Assert.notEmpty(permissionDTOs, "权限不存在");
			if(!param.isMultiApp()){
				for(PermissionDTO permissionDTO : permissionDTOs){
					Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appkey不匹配:" + permissionDTO.getId());
				}
			}
		}
	}
}
