package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.kingbo401.commons.encrypt.SecurityUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.MenuDAO;
import com.kingbo401.iceacl.dao.RoleDAO;
import com.kingbo401.iceacl.dao.RoleMenuRefDAO;
import com.kingbo401.iceacl.manager.AppManager;
import com.kingbo401.iceacl.manager.RoleManager;
import com.kingbo401.iceacl.manager.UserRoleRefManager;
import com.kingbo401.iceacl.model.db.MenuDO;
import com.kingbo401.iceacl.model.db.RoleDO;
import com.kingbo401.iceacl.model.db.RoleMenuRefDO;
import com.kingbo401.iceacl.model.db.param.RoleMenuQueryParam;
import com.kingbo401.iceacl.model.db.param.RoleQueryParam;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.param.RoleMenuIdRefParam;

import kingbo401.iceacl.common.constant.IceAclConstant;

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
	public RoleDTO getRoleByKey(String appKey, String roleKey) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(roleKey, "roleKey不能为空");
		RoleDO roleDO = roleDAO.getRoleByKey(appKey, roleKey);
		return buildRoleDTO(roleDO);
	}

	@Override
	public List<RoleDTO> getRoleByKeys(String appKey, List<String> roleKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleKeys, "roleKeys不能为空");
		List<RoleDO> roleDOs = roleDAO.getRoleByKeys(appKey, roleKeys);
		return buildRoleDTOs(roleDOs);
	}

	@Override
	public RoleDTO getRoleById(String appKey, long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(id > 0, "id必须大于0");
		RoleDO roleDO = roleDAO.getRoleById(id);
		if(roleDO == null){
			return null;
		}
		Assert.isTrue(roleDO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		return buildRoleDTO(roleDO);
	}

	@Override
	public RoleDTO getRoleById(long id) {
		Assert.isTrue(id > 0, "id必须大于0");
		RoleDO roleDO = roleDAO.getRoleById(id);
		if(roleDO == null){
			return null;
		}
		return buildRoleDTO(roleDO);
	}

	@Override
	public List<RoleDTO> getRoleByIds(String appKey, List<Long> roleIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		List<RoleDO> roleDOs = roleDAO.getRoleByIds(roleIds);
		if(CollectionUtils.isEmpty(roleDOs)){
			return null;
		}
		for(RoleDO roleDO : roleDOs){
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		}
		return buildRoleDTOs(roleDOs);
	}

	@Override
	public RoleDTO createRole(RoleDTO roleDTO) {
		Assert.notNull(roleDTO, "参数不能为空");
		String appKey = roleDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(appManager.getAppByKey(appKey), "app不存在");
		String tenant = roleDTO.getTenant();
		if(tenant == null){
			tenant = IceAclConstant.TENANT_COMMON_SYMBOL;
		}
		String roleKey = roleDTO.getRoleKey();
		if(StringUtils.isEmpty(roleKey)){
			roleKey = SecurityUtil.getUUID();
		}
		roleDTO.setRoleKey(roleKey);
		roleDTO.setStatus(IceAclConstant.STATUS_NORMAL);

		String roleName = roleDTO.getRoleName();
		Assert.hasText(roleName, "角色名不能为空");
		Assert.isTrue(roleName.length() < 100, "角色名不能大于100个字符");
		String description = roleDTO.getDescription();
		if(!StringUtils.isEmpty(description)){
			Assert.isTrue(description.length() < 100, "角色描述不能大于100个字符");
		}
		Date now = new Date();
		roleDTO.setCreateTime(now);
		roleDTO.setUpdateTime(now);
		RoleDO roleDO = roleDAO.getRoleByKey(appKey, roleKey);
		Assert.isNull(roleDO, "角色已存在");
		RoleQueryParam roleQueryParam = new RoleQueryParam();
		roleQueryParam.setAppKey(appKey);
		roleQueryParam.setTenant(tenant);
		roleQueryParam.setRoleName(roleName);
		List<RoleDO> namesRole = roleDAO.listRole(roleQueryParam);
		Assert.isTrue(CollectionUtils.isEmpty(namesRole), "角色名被使用");
		roleDO = new RoleDO();
		BeanUtils.copyProperties(roleDTO, roleDO);
		roleDAO.createRole(roleDO);
		BeanUtils.copyProperties(roleDO, roleDTO);
		return roleDTO;
	}

	@Override
	public RoleDTO updateRole(RoleDTO roleDTO) {
		Assert.notNull(roleDTO, "参数不能为空");
		String appKey = roleDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(appManager.getAppByKey(appKey), "app不存在");
		String tenant = roleDTO.getTenant();
		if(tenant == null){
			tenant = IceAclConstant.TENANT_COMMON_SYMBOL;
		}
		String roleKey = roleDTO.getRoleKey();
		if(StringUtils.isEmpty(roleKey)){
			roleKey = SecurityUtil.getUUID();
		}
		roleDTO.setRoleKey(roleKey);

		String roleName = roleDTO.getRoleName();
		Assert.hasText(roleName, "角色名不能为空");
		Assert.isTrue(roleName.length() < 100, "角色名不能大于100个字符");
		String description = roleDTO.getDescription();
		if(!StringUtils.isEmpty(description)){
			Assert.isTrue(description.length() < 100, "角色描述不能大于100个字符");
		}
		Date now = new Date();
		roleDTO.setUpdateTime(now);
		if(roleDTO.getStatus() == null){
			roleDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		}
		RoleDO roleDO = roleDAO.getRoleByKey(appKey, roleKey);
		Assert.notNull(roleDO, "角色已存在");
		Assert.isTrue(tenant.equals(roleDO.getTenant()), "tenant不能修改");
		if(!roleName.equals(roleDO.getRoleName())){
			RoleQueryParam roleQueryParam = new RoleQueryParam();
			roleQueryParam.setAppKey(appKey);
			roleQueryParam.setTenant(tenant);
			roleQueryParam.setRoleName(roleName);
			List<RoleDO> namesRole = roleDAO.listRole(roleQueryParam);
			Assert.isTrue(CollectionUtils.isEmpty(namesRole), "角色名被使用");
		}
		BeanUtils.copyProperties(roleDTO, roleDO);
		roleDO.setUpdateTime(now);
		roleDAO.updateRole(roleDO);
		return roleDTO;
	}

	private boolean updateRoleStatus(String appKey, long roleId, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(roleId > 0, "roleId必须大于0");
		if(status == IceAclConstant.STATUS_REMOVE){
			Assert.isTrue(!userRoleRefManager.hasUserUse(roleId), "角色有用户使用");
		}
		RoleDO roleDO = roleDAO.getRoleById(roleId);
		Assert.notNull(roleDO, "角色不存在");
		Assert.isTrue(appKey.equals(roleDO.getAppKey()), "appkey角色不匹配");
		roleDO.setUpdateTime(new Date());
		roleDO.setStatus(status);
		roleDAO.updateRole(roleDO);
		return true;
	}
	
	@Override
	public boolean removeRole(String appKey, long roleId) {
		return updateRoleStatus(appKey, roleId, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeRole(String appKey, long roleId) {
		return updateRoleStatus(appKey, roleId, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeRole(String appKey, long roleId) {
		return updateRoleStatus(appKey, roleId, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public List<RoleDTO> listRole(RoleQueryParam param) {
		Assert.notNull(param);
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

	@Override
	public boolean updateRoleMenuRef(RoleMenuIdRefParam param) {
		Assert.notNull(param, "参数不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Long roleId = param.getRoleId();
		Assert.notNull(roleId, "roleId不能为null");
		RoleDTO roleDTO = getRoleById(appKey, roleId);
		Assert.notNull(roleDTO, "角色不存在");
		boolean multiApp = param.isMultiApp();
		RoleMenuQueryParam roleMenuQueryParam = new RoleMenuQueryParam();
		if(!multiApp){
			roleMenuQueryParam.setAppKey(param.getAppKey());
		}
		roleMenuQueryParam.setRoleId(roleId);
		List<MenuDO> menuDOs = roleMenuRefDAO.listMenu(roleMenuQueryParam);
		if(!CollectionUtils.isEmpty(menuDOs)){
			List<Long> menuIds = new ArrayList<Long>();
			for(MenuDO menuDO : menuDOs){
				menuIds.add(menuDO.getId());
			}
			roleMenuRefDAO.updateRefsStatus(roleId, menuIds, IceAclConstant.STATUS_REMOVE);
		}
		
		List<Long> menuIds = param.getMenuIds();
		if(CollectionUtils.isEmpty(menuIds)){
			return true;
		}
		
	    Map<Object, MenuDO> menuMap = CollectionUtil.toIdMap(menuDAO.getMenuByIds(menuIds));
		List<RoleMenuRefDO> refDOs = new ArrayList<RoleMenuRefDO>();
		Date now = new Date();
		for(Long menuId : menuIds){
			Assert.notNull(menuMap.get(menuId), "菜单:" + menuId + " 不存在");
			RoleMenuRefDO refDO = new RoleMenuRefDO();
			refDO.setCreateTime(now);
			refDO.setUpdateTime(now);
			refDO.setMenuId(menuId);
			refDO.setRoleId(roleId);
			refDO.setStatus(IceAclConstant.STATUS_NORMAL);
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
