package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.kingbo401.commons.encrypt.SecurityUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.dao.MenuDAO;
import com.kingbo401.iceacl.dao.RoleDAO;
import com.kingbo401.iceacl.dao.RoleMenuRefDAO;
import com.kingbo401.iceacl.manager.AppManager;
import com.kingbo401.iceacl.manager.RoleManager;
import com.kingbo401.iceacl.manager.UserRoleRefManager;
import com.kingbo401.iceacl.model.dto.RoleDTO;
import com.kingbo401.iceacl.model.dto.param.RoleMenuIdRefParam;
import com.kingbo401.iceacl.model.po.MenuPO;
import com.kingbo401.iceacl.model.po.RoleMenuRefPO;
import com.kingbo401.iceacl.model.po.RolePO;
import com.kingbo401.iceacl.model.po.param.RoleMenuQueryParam;
import com.kingbo401.iceacl.model.po.param.RoleQueryParam;

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
		RolePO rolePO = roleDAO.getRoleByKey(appKey, roleKey);
		return buildRoleDTO(rolePO);
	}

	@Override
	public List<RoleDTO> getRoleByKeys(String appKey, List<String> roleKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleKeys, "roleKeys不能为空");
		List<RolePO> rolePOs = roleDAO.getRoleByKeys(appKey, roleKeys);
		return buildRoleDTOs(rolePOs);
	}

	@Override
	public RoleDTO getRoleById(String appKey, long id) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(id > 0, "id必须大于0");
		RolePO rolePO = roleDAO.getRoleById(id);
		if(rolePO == null){
			return null;
		}
		Assert.isTrue(rolePO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		return buildRoleDTO(rolePO);
	}

	@Override
	public RoleDTO getRoleById(long id) {
		Assert.isTrue(id > 0, "id必须大于0");
		RolePO rolePO = roleDAO.getRoleById(id);
		if(rolePO == null){
			return null;
		}
		return buildRoleDTO(rolePO);
	}

	@Override
	public List<RoleDTO> getRoleByIds(String appKey, List<Long> roleIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(roleIds, "roleIds不能为空");
		List<RolePO> rolePOs = roleDAO.getRoleByIds(roleIds);
		if(CollectionUtils.isEmpty(rolePOs)){
			return null;
		}
		for(RolePO rolePO : rolePOs){
			Assert.isTrue(rolePO.getAppKey().equals(appKey), "appKey、roleId不匹配");
		}
		return buildRoleDTOs(rolePOs);
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
		RolePO rolePO = roleDAO.getRoleByKey(appKey, roleKey);
		Assert.isNull(rolePO, "角色已存在");
		RoleQueryParam roleQueryParam = new RoleQueryParam();
		roleQueryParam.setAppKey(appKey);
		roleQueryParam.setTenant(tenant);
		roleQueryParam.setRoleName(roleName);
		List<RolePO> namesRole = roleDAO.listRole(roleQueryParam);
		Assert.isTrue(CollectionUtils.isEmpty(namesRole), "角色名被使用");
		rolePO = new RolePO();
		BeanUtils.copyProperties(roleDTO, rolePO);
		roleDAO.createRole(rolePO);
		BeanUtils.copyProperties(rolePO, roleDTO);
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
		RolePO rolePO = roleDAO.getRoleByKey(appKey, roleKey);
		Assert.notNull(rolePO, "角色已存在");
		Assert.isTrue(tenant.equals(rolePO.getTenant()), "tenant不能修改");
		if(!roleName.equals(rolePO.getRoleName())){
			RoleQueryParam roleQueryParam = new RoleQueryParam();
			roleQueryParam.setAppKey(appKey);
			roleQueryParam.setTenant(tenant);
			roleQueryParam.setRoleName(roleName);
			List<RolePO> namesRole = roleDAO.listRole(roleQueryParam);
			Assert.isTrue(CollectionUtils.isEmpty(namesRole), "角色名被使用");
		}
		BeanUtils.copyProperties(roleDTO, rolePO);
		rolePO.setUpdateTime(now);
		roleDAO.updateRole(rolePO);
		return roleDTO;
	}

	private boolean updateRoleStatus(String appKey, long roleId, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.isTrue(roleId > 0, "roleId必须大于0");
		if(status == IceAclConstant.STATUS_REMOVE){
			Assert.isTrue(!userRoleRefManager.hasUserUse(roleId), "角色有用户使用");
		}
		RolePO rolePO = roleDAO.getRoleById(roleId);
		Assert.notNull(rolePO, "角色不存在");
		Assert.isTrue(appKey.equals(rolePO.getAppKey()), "appkey角色不匹配");
		rolePO.setUpdateTime(new Date());
		rolePO.setStatus(status);
		roleDAO.updateRole(rolePO);
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
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		List<RolePO> rolePOs = roleDAO.listRole(param);
		return buildRoleDTOs(rolePOs);
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
		List<RolePO> rolePOs = roleDAO.pageRole(param);
		pageVO.setItems(buildRoleDTOs(rolePOs));
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
		RoleMenuQueryParam roleMenuQueryParam = new RoleMenuQueryParam();
		roleMenuQueryParam.setAppKey(param.getAppKey());
		roleMenuQueryParam.setRoleId(roleId);
		List<MenuPO> menuPOs = roleMenuRefDAO.listMenu(roleMenuQueryParam);
		if(!CollectionUtils.isEmpty(menuPOs)){
			List<Long> menuIds = new ArrayList<Long>();
			for(MenuPO menuPO : menuPOs){
				menuIds.add(menuPO.getId());
			}
			roleMenuRefDAO.updateRefsStatus(roleId, menuIds, IceAclConstant.STATUS_REMOVE);
		}
		
		List<Long> menuIds = param.getMenuIds();
		if(CollectionUtils.isEmpty(menuIds)){
			return true;
		}
		
	    Map<Object, MenuPO> menuMap = menuDAO.getMenuByIds(menuIds).stream().collect(Collectors.toMap(MenuPO::getId, a -> a, (k1, k2) -> k1));;
		List<RoleMenuRefPO> refPOs = new ArrayList<RoleMenuRefPO>();
		Date now = new Date();
		for(Long menuId : menuIds){
			Assert.notNull(menuMap.get(menuId), "菜单:" + menuId + " 不存在");
			RoleMenuRefPO refPO = new RoleMenuRefPO();
			refPO.setCreateTime(now);
			refPO.setUpdateTime(now);
			refPO.setMenuId(menuId);
			refPO.setRoleId(roleId);
			refPO.setStatus(IceAclConstant.STATUS_NORMAL);
			refPOs.add(refPO);
		}
		roleMenuRefDAO.batchCreate(refPOs);
		return true;
	}
	
	private RoleDTO buildRoleDTO(RolePO rolePO){
		if(rolePO == null){
			return null;
		}
		RoleDTO roleDTO = new RoleDTO();
		BeanUtils.copyProperties(rolePO, roleDTO);
		return roleDTO;
	}
	
	private List<RoleDTO> buildRoleDTOs(List<RolePO> rolePOs){
		if(CollectionUtils.isEmpty(rolePOs)){
			return null;
		}
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		for(RolePO rolePO : rolePOs){
			roleDTOs.add(buildRoleDTO(rolePO));
		}
		return roleDTOs;
	}
}
