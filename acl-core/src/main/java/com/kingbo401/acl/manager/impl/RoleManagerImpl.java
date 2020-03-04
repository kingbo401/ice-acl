package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.model.dto.RoleDTO;
import com.kingbo401.acl.common.model.dto.param.RoleMenuIdRefParam;
import com.kingbo401.acl.dao.MenuDAO;
import com.kingbo401.acl.dao.RoleDAO;
import com.kingbo401.acl.dao.RoleMenuRefDAO;
import com.kingbo401.acl.manager.AppManager;
import com.kingbo401.acl.manager.RoleManager;
import com.kingbo401.acl.manager.UserRoleRefManager;
import com.kingbo401.acl.model.entity.MenuDO;
import com.kingbo401.acl.model.entity.RoleDO;
import com.kingbo401.acl.model.entity.RoleMenuRefDO;
import com.kingbo401.acl.model.entity.param.RoleMenuQueryParam;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;
import com.kingbo401.commons.encrypt.SecurityUtil;
import com.kingbo401.commons.model.PageVO;

@Service
public class RoleManagerImpl implements RoleManager{
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private AppManager appManager;
	@Autowired
	private RoleMenuRefDAO roleMenuRefDAO;
	@Autowired
	private MenuDAO menuDAO;
	@Autowired
	private UserRoleRefManager userRoleRefManager;
	
	@Override
	public RoleDTO getByKey(String appKey, String roleKey) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(roleKey, "roleKey不能为空");
		RoleDO roleDO = roleDAO.getByKey(appKey, roleKey);
		return buildRoleDTO(roleDO);
	}

	@Override
	public List<RoleDTO> getByKeys(String appKey, List<String> roleKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleKeys, "roleKeys不能为空");
		List<RoleDO> roleDOs = roleDAO.getByKeys(appKey, roleKeys);
		return buildRoleDTOs(roleDOs);
	}

	@Override
	public RoleDTO getById(String appKey, long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(id > 0, "id必须大于0");
		RoleDO roleDO = roleDAO.getById(id);
		if(roleDO == null){
			return null;
		}
		Assert.isTrue(roleDO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		return buildRoleDTO(roleDO);
	}

	@Override
	public RoleDTO getById(long id) {
		Assert.isTrue(id > 0, "id必须大于0");
		RoleDO roleDO = roleDAO.getById(id);
		if(roleDO == null){
			return null;
		}
		return buildRoleDTO(roleDO);
	}

	@Override
	public List<RoleDTO> getByIds(String appKey, List<Long> roleIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		List<RoleDO> roleDOs = roleDAO.getByIds(roleIds);
		if(CollectionUtils.isEmpty(roleDOs)){
			return null;
		}
		for(RoleDO roleDO : roleDOs){
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		}
		return buildRoleDTOs(roleDOs);
	}

	@Override
	public RoleDTO save(RoleDTO roleDTO) {
		String appKey = roleDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		if(roleDTO.getTenant() == null){
			roleDTO.setTenant(AclConstant.TENANT_COMMON);
		}
		if (roleDTO.getSubgroup() == null) {
			roleDTO.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		if(roleDTO.getStatus() == null){
			roleDTO.setStatus(AclConstant.STATUS_NORMAL);
		}
		String roleKey = roleDTO.getRoleKey();
		if(StringUtils.isEmpty(roleKey)){
			roleKey = SecurityUtil.getUUID();
			roleDTO.setRoleKey(roleKey);
		}
		String roleName = roleDTO.getName();
		Assert.hasText(roleName, "角色名不能为空");
		Assert.notNull(appManager.getByKey(appKey), "应用不存在");
		RoleDO roleDO = roleDAO.getByKey0(appKey, roleKey);
		if (roleDO != null && !roleDO.getStatus().equals(AclConstant.STATUS_REMOVE)) {
			Assert.isTrue(roleDTO.getTenant().equals(roleDO.getTenant()), "tenant不能修改");
			if(!roleName.equals(roleDO.getName())){
				RoleQueryParam roleQueryParam = new RoleQueryParam();
				roleQueryParam.setAppKey(appKey);
				roleQueryParam.setTenant(roleDTO.getTenant());
				roleQueryParam.setName(roleName);
				roleQueryParam.setFuzzy(false);
				List<RoleDO> namesRole = roleDAO.listRole(roleQueryParam);
				Assert.isTrue(CollectionUtils.isEmpty(namesRole), "角色名被使用");
			}
			BeanUtils.copyProperties(roleDTO, roleDO);
			roleDAO.update(roleDO);
		} if (roleDO != null) {
			BeanUtils.copyProperties(roleDTO, roleDO);
			roleDAO.update(roleDO);
		} else {
			roleDO = new RoleDO();
			BeanUtils.copyProperties(roleDTO, roleDO);
			roleDAO.create(roleDO);
		}
		roleDTO.setId(roleDO.getId());
		RoleMenuIdRefParam roleMenuIdRefParam = new RoleMenuIdRefParam();
		roleMenuIdRefParam.setAppKey(appKey);
		roleMenuIdRefParam.setMenuIds(roleDTO.getMenuIds());
		roleMenuIdRefParam.setOperator(roleDTO.getModifier());
		roleMenuIdRefParam.setRoleId(roleDTO.getId());
		roleMenuIdRefParam.setSubgroup(roleDTO.getMenuSubgroup());
		this.updateRoleMenuRef(roleMenuIdRefParam);
		return roleDTO;
	}

	private boolean updateRoleStatus(RoleDTO roleDTO, int status){
		String appKey = roleDTO.getAppKey();
		Long roleId = roleDTO.getId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(roleId > 0, "roleId必须大于0");
		if(status == AclConstant.STATUS_REMOVE){
			Assert.isTrue(!userRoleRefManager.hasUserUse(roleId), "角色有用户使用");
		}
		RoleDO roleDO = roleDAO.getById(roleId);
		Assert.notNull(roleDO, "角色不存在");
		Assert.isTrue(appKey.equals(roleDO.getAppKey()), "appkey角色不匹配");
		roleDO.setStatus(status);
		roleDAO.update(roleDO);
		return true;
	}
	
	@Override
	public boolean remove(RoleDTO roleDTO) {
		return updateRoleStatus(roleDTO, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freeze(RoleDTO roleDTO) {
		return updateRoleStatus(roleDTO, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreeze(RoleDTO roleDTO) {
		return updateRoleStatus(roleDTO, AclConstant.STATUS_NORMAL);
	}

	@Override
	public List<RoleDTO> listRole(RoleQueryParam param) {
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		List<RoleDO> roleDOs = roleDAO.listRole(param);
		return buildRoleDTOs(roleDOs);
	}

	@Override
	public PageVO<RoleDTO> pageRole(RoleQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		
		PageVO<RoleDTO> pageVO = new PageVO<RoleDTO>(param);
		if(param.isReturnTotalCount()){
			long total = roleDAO.countRole(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<RoleDO> roleDOs = roleDAO.pageRole(param);
		pageVO.setItems(buildRoleDTOs(roleDOs));
		return pageVO;
	}

	private boolean updateRoleMenuRef(RoleMenuIdRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为null");
		RoleDTO roleDTO = getById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		if (param.getSubgroup() == null) {
			param.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		RoleMenuQueryParam roleMenuQueryParam = new RoleMenuQueryParam();
		roleMenuQueryParam.setRoleId(roleId);
		roleMenuQueryParam.setSubgroup(param.getSubgroup());
		List<MenuDO> menuDOs = roleMenuRefDAO.listMenu(roleMenuQueryParam);
		if(CollectionUtils.isNotEmpty(menuDOs)){
			List<Long> menuIds = menuDOs.stream().map(MenuDO::getId).collect(Collectors.toList());
			roleMenuRefDAO.updateRefsStatus(roleId, menuIds, AclConstant.STATUS_REMOVE);
		}
		
		List<Long> menuIds = param.getMenuIds();
		if(CollectionUtils.isEmpty(menuIds)){
			return true;
		}
		
	    Map<Object, MenuDO> menuMap = menuDAO.getByIds(menuIds).stream().collect(Collectors.toMap(MenuDO::getId, a -> a, (k1, k2) -> k1));;
		List<RoleMenuRefDO> refDOs = new ArrayList<RoleMenuRefDO>();
		for(Long menuId : menuIds){
			Assert.notNull(menuMap.get(menuId), "菜单:" + menuId + " 不存在");
			RoleMenuRefDO refDO = new RoleMenuRefDO();
			refDO.setMenuId(menuId);
			refDO.setRoleId(roleId);
			refDO.setStatus(AclConstant.STATUS_NORMAL);
			refDOs.add(refDO);
		}
		roleMenuRefDAO.batchCreate(refDOs);
		return true;
	}
	
	private RoleDTO buildRoleDTO(RoleDO roleDO){
		if(roleDO == null){
			return null;
		}
		RoleDTO roleDTO = new RoleDTO();
		BeanUtils.copyProperties(roleDO, roleDTO);
		return roleDTO;
	}
	
	private List<RoleDTO> buildRoleDTOs(List<RoleDO> roleDOs){
		if(CollectionUtils.isEmpty(roleDOs)){
			return null;
		}
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		for(RoleDO roleDO : roleDOs){
			roleDTOs.add(buildRoleDTO(roleDO));
		}
		return roleDTOs;
	}
}
