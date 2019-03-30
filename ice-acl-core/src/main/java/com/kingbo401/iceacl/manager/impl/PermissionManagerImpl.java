package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.PermissionDAO;
import com.kingbo401.iceacl.manager.PermissionManager;
import com.kingbo401.iceacl.manager.UserPermissionRefManager;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.param.PermissonQueryParam;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;

@Service
public class PermissionManagerImpl implements PermissionManager{
	@Autowired
	private PermissionDAO permissionDAO;
	@Autowired
	private UserPermissionRefManager userPermissionRefManager;

	private boolean updatePermissionStatus(String appKey, Long id, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(id, "permissionId不能为空");
		PermissionPO permissionPO = permissionDAO.getById(id);
		Assert.notNull(permissionPO, "权限不存在");
		Assert.isTrue(appKey.equals(permissionPO.getAppKey()), "appKey, permissionId不匹配");
		permissionPO.setStatus(status);
		permissionDAO.update(permissionPO);
		return true;
	}
	@Override
	public boolean removePermission(String appKey, Long id) {
		Assert.isTrue(!userPermissionRefManager.hasUserUse(id), "权限有用户使用");
		return updatePermissionStatus(appKey, id, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean unfreezePermission(String appKey, Long id) {
		return updatePermissionStatus(appKey, id, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public boolean freezePermission(String appKey, Long id) {
		return updatePermissionStatus(appKey, id, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public PermissionDTO getPermissionByKey(String appKey, String permissionKey) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(permissionKey, "permissionKey不能为空");
		PermissionPO permissionPO = permissionDAO.getByKey(appKey, permissionKey);
		return BizUtils.buildPermissionDTO(permissionPO);
	}

	@Override
	public PermissionDTO getPermissionById(String appKey, Long permissionId) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(permissionId, "permissionId不能为空");
		PermissionPO permissionPO = permissionDAO.getById(permissionId);
		if(permissionPO == null){
			return null;
		}
		Assert.isTrue(appKey.equals(permissionPO.getAppKey()), "appKey, permissionId不匹配");
		return BizUtils.buildPermissionDTO(permissionPO);
	}

	@Override
	public List<PermissionDTO> getPermissionByKeys(String appKey, List<String> permissionKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionKeys, "permissionKeys不能为空");
		List<PermissionPO> permissionPOs = permissionDAO.getPermissionByKeys(appKey, permissionKeys);
		return BizUtils.buildPermissionDTOs(permissionPOs);
	}

	@Override
	public List<PermissionDTO> getPermissionByIds(String appKey, List<Long> permissionIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		List<PermissionPO> permissionPOs = permissionDAO.getPermissionByIds(permissionIds);
		Assert.notEmpty(permissionPOs, "权限不存在");
		for(PermissionPO permissionPO : permissionPOs){
			Assert.isTrue(appKey.equals(permissionPO.getAppKey()), "appKey, permissionId不匹配");
		}
		Map<Object, PermissionPO> permissionMap = CollectionUtil.toIdMap(permissionPOs);
		for(Long permissionId : permissionIds){
			Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
		}
		return BizUtils.buildPermissionDTOs(permissionPOs);
	}
	
	@Override
	public List<PermissionDTO> getPermissionByIds(List<Long> permissionIds) {
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		List<PermissionPO> permissionPOs = permissionDAO.getPermissionByIds(permissionIds);
		Assert.notEmpty(permissionPOs, "权限不存在");
		Map<Object, PermissionPO> permissionMap = CollectionUtil.toIdMap(permissionPOs);
		for(Long permissionId : permissionIds){
			Assert.notNull(permissionMap.get(permissionId), "权限:" + permissionId + " 不存在");
		}
		return BizUtils.buildPermissionDTOs(permissionPOs);
	}

	@Override
	public List<PermissionDTO> listPermission(PermissonQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.notNull(param.getAppKey(), "appKey不能为空");
		List<PermissionPO> permissionPOs = permissionDAO.listPermission(param);
		return BizUtils.buildPermissionDTOs(permissionPOs);
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
		List<PermissionPO> permissionPOs = permissionDAO.listPermission(param);
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionPOs));
		return pageVO;
	}

	@Override
	public PermissionDTO createPermission(PermissionDTO permissionDTO) {
		Assert.notNull(permissionDTO, "参数不能为空");
		String appKey = permissionDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String permissionKey = permissionDTO.getPermissionKey();
		Assert.hasText(permissionKey, "permissionKey不能为空");
		Assert.hasText(permissionDTO.getPermissionName(), "permissionName不能为空");
		permissionDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		Date now = new Date();
		permissionDTO.setCreateTime(now);
		permissionDTO.setUpdateTime(now);
		PermissionPO permissoinPO = permissionDAO.getByKey(appKey, permissionKey);
		Assert.isNull(permissoinPO, "权限已存在");
		permissoinPO = new PermissionPO();
		BeanUtils.copyProperties(permissionDTO, permissoinPO);
		permissionDAO.create(permissoinPO);
		BeanUtils.copyProperties(permissoinPO, permissionDTO);
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
		Assert.hasText(permissionDTO.getPermissionName(), "permissionName不能为空");
		PermissionPO permissoinPO = permissionDAO.getById(id);
		Assert.notNull(permissoinPO, "权限不存在");
		Date now = new Date();
		permissoinPO.setUpdateTime(now);
		permissoinPO.setPermissionKey(permissionKey);
		permissoinPO.setDescription(permissionDTO.getDescription());
		permissoinPO.setPermissionName(permissionDTO.getPermissionName());
		permissoinPO.setPermissionType(permissionDTO.getPermissionType());
		permissionDAO.update(permissoinPO);
		BeanUtils.copyProperties(permissoinPO, permissionDTO);
		return permissionDTO;
	}

	@Override
	public int createPermissions(String appKey, List<PermissionDTO> permissions) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissions, "权限不能为空");
		List<PermissionPO> permissionPOs = new ArrayList<PermissionPO>();
		Date now = new Date();
		for(PermissionDTO permissionDTO : permissions){
			Assert.hasText(permissionDTO.getPermissionKey(), "permissionKey不能为空");
			Assert.hasText(permissionDTO.getPermissionName(), "permissionName不能为空");
			if(StringUtils.isEmpty(permissionDTO.getAppKey())){
				permissionDTO.setAppKey(appKey);
			}
			Assert.isTrue(appKey.equals(permissionDTO.getAppKey()), "权限appKey不匹配");
			permissionDTO.setCreateTime(now);
			permissionDTO.setUpdateTime(now);
			PermissionPO permissionPO = new PermissionPO();
			BeanUtils.copyProperties(permissionDTO, permissionPO);
			permissionPOs.add(permissionPO);
		}
		return permissionDAO.batchCreate(permissionPOs);
	}
}
