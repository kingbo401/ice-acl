package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kingbo401.acl.dao.PermissionDAO;
import com.kingbo401.acl.manager.PermissionManager;
import com.kingbo401.acl.manager.UserPermissionRefManager;
import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.param.PermissonQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class PermissionManagerImpl implements PermissionManager{
	@Autowired
	private PermissionDAO permissionDAO;
	@Autowired
	private UserPermissionRefManager userPermissionRefManager;

	private boolean updatePermissionStatus(PermissionDTO permissionDTO, int status){
		String appKey = permissionDTO.getAppKey();
		Long id = permissionDTO.getId();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "permissionId不能为空");
		PermissionDO permissionDO = permissionDAO.getById(id);
		Assert.notNull(permissionDO, "权限不存在");
		Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		permissionDO.setStatus(status);
		permissionDAO.update(permissionDO);
		return true;
	}
	@Override
	public boolean removePermission(PermissionDTO permissionDTO) {
		Assert.isTrue(!userPermissionRefManager.hasUserUse(permissionDTO.getId()), "权限有用户使用");
		return updatePermissionStatus(permissionDTO, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean unfreezePermission(PermissionDTO permissionDTO) {
		return updatePermissionStatus(permissionDTO, AclConstant.STATUS_NORMAL);
	}

	@Override
	public boolean freezePermission(PermissionDTO permissionDTO) {
		return updatePermissionStatus(permissionDTO, AclConstant.STATUS_FREEZE);
	}

	@Override
	public PermissionDTO getPermissionByKey(String appKey, String permissionKey) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(permissionKey, "permissionKey不能为空");
		PermissionDO permissionDO = permissionDAO.getByKey(appKey, permissionKey);
		return BizUtil.buildPermissionDTO(permissionDO);
	}

	@Override
	public PermissionDTO getPermissionById(String appKey, Long permissionId) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(permissionId, "permissionId不能为空");
		PermissionDO permissionDO = permissionDAO.getById(permissionId);
		if(permissionDO == null){
			return null;
		}
		Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		return BizUtil.buildPermissionDTO(permissionDO);
	}

	@Override
	public List<PermissionDTO> getPermissionByKeys(String appKey, List<String> permissionKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionKeys, "permissionKeys不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.getPermissionByKeys(appKey, permissionKeys);
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public List<PermissionDTO> getPermissionByIds(String appKey, List<Long> permissionIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.getPermissionByIds(permissionIds);
		Assert.notEmpty(permissionDOs, "权限不存在");
		for(PermissionDO permissionDO : permissionDOs){
			Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		}
		Map<Object, PermissionDO> permissionMap = permissionDOs.stream().collect(Collectors.toMap(PermissionDO::getId, a -> a, (k1, k2) -> k1));
		for(Long permissionId : permissionIds){
			Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
		}
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}
	
	@Override
	public List<PermissionDTO> getPermissionByIds(List<Long> permissionIds) {
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.getPermissionByIds(permissionIds);
		Assert.notEmpty(permissionDOs, "权限不存在");
		Map<Long, PermissionDO> permissionMap = permissionDOs.stream().collect(Collectors.toMap(PermissionDO::getId, a -> a, (k1, k2) -> k1));
		for(Long permissionId : permissionIds){
			Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
		}
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public List<PermissionDTO> listPermission(PermissonQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.notNull(param.getAppKey(), "appKey不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.listPermission(param);
		return BizUtil.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public PageVO<PermissionDTO> pagePermission(PermissonQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.notNull(param.getAppKey(), "appKey不能为空");
		PageVO<PermissionDTO> pageVO = new PageVO<PermissionDTO>(param);
		if(param.isReturnTotalCount()){
			long total = permissionDAO.countPermission(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<PermissionDO> permissionDOs = permissionDAO.listPermission(param);
		pageVO.setItems(BizUtil.buildPermissionDTOs(permissionDOs));
		return pageVO;
	}

	@Override
	public PermissionDTO createPermission(PermissionDTO permissionDTO) {
		Assert.notNull(permissionDTO, "参数不能为空");
		String appKey = permissionDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String permissionKey = permissionDTO.getPermissionKey();
		Assert.hasText(permissionKey, "permissionKey不能为空");
		Assert.hasText(permissionDTO.getName(), "permissionName不能为空");
		permissionDTO.setStatus(AclConstant.STATUS_NORMAL);
		if (permissionDTO.getSubgroup() == null) {
			permissionDTO.setSubgroup(AclConstant.DEF_SUBGROUP);
		}
		PermissionDO permissoinDO = permissionDAO.getByKey(appKey, permissionKey);
		Assert.isNull(permissoinDO, "权限已存在");
		permissoinDO = new PermissionDO();
		BeanUtils.copyProperties(permissionDTO, permissoinDO);
		permissionDAO.create(permissoinDO);
		BeanUtils.copyProperties(permissoinDO, permissionDTO);
		return permissionDTO;
	}

	@Override
	public PermissionDTO updatePermission(PermissionDTO permissionDTO) {
		Assert.notNull(permissionDTO, "参数不能为空");
		Long id = permissionDTO.getId();
		Assert.notNull(id, "权限id不能为空");
		String appKey = permissionDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String permissionKey = permissionDTO.getPermissionKey();
		Assert.hasText(permissionKey, "permissionKey不能为空");
		Assert.hasText(permissionDTO.getName(), "permissionName不能为空");
		PermissionDO permissoinDO = permissionDAO.getById(id);
		Assert.notNull(permissoinDO, "权限不存在");
		BeanUtils.copyProperties(permissionDTO, permissoinDO);
		permissionDAO.update(permissoinDO);
		return permissionDTO;
	}

	@Override
	public int createPermissions(String appKey, List<PermissionDTO> permissions) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissions, "权限不能为空");
		List<PermissionDO> permissionDOs = new ArrayList<PermissionDO>();
		for(PermissionDTO permissionDTO : permissions){
			Assert.hasText(permissionDTO.getPermissionKey(), "permissionKey不能为空");
			Assert.hasText(permissionDTO.getName(), "permissionName不能为空");
			if(StringUtils.isEmpty(permissionDTO.getAppKey())){
				permissionDTO.setAppKey(appKey);
			}
			Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appKey不匹配");
			PermissionDO permissionDO = new PermissionDO();
			BeanUtils.copyProperties(permissionDTO, permissionDO);
			permissionDOs.add(permissionDO);
		}
		return permissionDAO.batchCreate(permissionDOs);
	}
}
