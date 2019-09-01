package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.RolePermissionRefDAO;
import com.kingbo401.acl.manager.PermissionManager;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.manager.RolePermissionRefManager;
import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.RoleDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionIdRefParam;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.RolePermissionRefDO;
import com.kingbo401.acl.model.entity.param.RolePermissionRefQueryParam;
import com.kingbo401.acl.utils.BizUtils;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

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
			rolePermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
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
		RolePermissionRefQueryParam permissionGroupRefQueryParam = new RolePermissionRefQueryParam();
		permissionGroupRefQueryParam.setRoleId(roleId);
		List<PermissionDO> permissionPOs =  rolePermissionRefDAO.listPermission(permissionGroupRefQueryParam);
		if(CollectionUtil.isNotEmpty(permissionPOs)){
			List<Long> permissionIdsRemove = new ArrayList<Long>();
			for(PermissionDO permissionDO : permissionPOs){
				permissionIdsRemove.add(permissionDO.getId());
			}
			rolePermissionRefDAO.updateRefsStatus(roleId, permissionIdsRemove, AclConstant.STATUS_REMOVE);
		}
		
		List<RolePermissionRefDO> list = new ArrayList<RolePermissionRefDO>();
		Date now = new Date();
		for(Long permissionId : permissionIds){
			RolePermissionRefDO rolePermissionRefDO = new RolePermissionRefDO();
			rolePermissionRefDO.setPermissionId(permissionId);
			rolePermissionRefDO.setRoleId(roleId);
			rolePermissionRefDO.setCreateTime(now);
			rolePermissionRefDO.setUpdateTime(now);
			rolePermissionRefDO.setStatus(AclConstant.STATUS_NORMAL);
			list.add(rolePermissionRefDO);
		}
		rolePermissionRefDAO.batchCreate(list);
		return true;
	}

	@Override
	public boolean removeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), AclConstant.STATUS_REMOVE);
		return true;
	}

	@Override
	public boolean freezeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), AclConstant.STATUS_FREEZE);
		return true;
	}

	@Override
	public boolean unfreezeRolePermissionRef(RolePermissionIdRefParam param) {
		checkRolePermissionIdRefParam(param);
		rolePermissionRefDAO.updateRefsStatus(param.getRoleId(), param.getPermissionIds(), AclConstant.STATUS_NORMAL);
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
		List<PermissionDO> permissionPOs = rolePermissionRefDAO.listPermission(param);
		return BizUtils.buildPermissionDTOs(permissionPOs);
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
		List<PermissionDO> permissionPOs = rolePermissionRefDAO.pagePermission(param);
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionPOs));
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
			for(PermissionDTO permissionDTO : permissionDTOs){
				Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appkey不匹配:" + permissionDTO.getId());
			}
		}
	}
}
