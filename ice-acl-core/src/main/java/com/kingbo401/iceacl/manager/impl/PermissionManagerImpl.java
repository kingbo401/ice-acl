package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.dao.PermissionDAO;
import com.kingbo401.iceacl.manager.PermissionManager;
import com.kingbo401.iceacl.manager.UserPermissionRefManager;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.param.PermissonQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
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
		PermissionDO permissionDO = permissionDAO.getById(id);
		Assert.notNull(permissionDO, "权限不存在");
		Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		permissionDO.setStatus(status);
		permissionDAO.update(permissionDO);
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
		PermissionDO permissionDO = permissionDAO.getByKey(appKey, permissionKey);
		return BizUtils.buildPermissionDTO(permissionDO);
	}

	@Override
	public PermissionDTO getPermissionById(String appKey, Long permissionId) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(permissionId, "permissionId不能为空");
		PermissionDO permissionDO = permissionDAO.getById(permissionId);
		Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		return BizUtils.buildPermissionDTO(permissionDO);
	}

	@Override
	public List<PermissionDTO> getPermissionByKeys(String appKey, List<String> permissionKeys) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionKeys, "permissionKeys不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.getPermissionByKeys(appKey, permissionKeys);
		return BizUtils.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public List<PermissionDTO> getPermissionByIds(String appKey, List<Long> permissionIds) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissionIds, "permissionIds不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.getPermissionByIds(permissionIds);
		if(CollectionUtils.isEmpty(permissionDOs)){
			return null;
		}
		for(PermissionDO permissionDO : permissionDOs){
			Assert.isTrue(appKey.equals(permissionDO.getAppKey()), "appKey, permissionId不匹配");
		}
		return BizUtils.buildPermissionDTOs(permissionDOs);
	}

	@Override
	public List<PermissionDTO> listPermission(PermissonQueryParam param) {
		Assert.notNull(param, "参数不能为空");
		Assert.notNull(param.getAppKey(), "appKey不能为空");
		List<PermissionDO> permissionDOs = permissionDAO.listPermission(param);
		return BizUtils.buildPermissionDTOs(permissionDOs);
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
		pageVO.setItems(BizUtils.buildPermissionDTOs(permissionDOs));
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
		Assert.hasText(permissionDTO.getPermissionName(), "permissionName不能为空");
		PermissionDO permissoinDO = permissionDAO.getById(id);
		Assert.notNull(permissoinDO, "权限不存在");
		Date now = new Date();
		permissoinDO.setUpdateTime(now);
		permissoinDO.setPermissionKey(permissionKey);
		permissoinDO.setDescription(permissionDTO.getDescription());
		permissoinDO.setPermissionName(permissionDTO.getPermissionName());
		permissoinDO.setPermissionType(permissionDTO.getPermissionType());
		permissionDAO.update(permissoinDO);
		BeanUtils.copyProperties(permissoinDO, permissionDTO);
		return permissionDTO;
	}

	@Override
	public int createPermissions(String appKey, List<PermissionDTO> permissions) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notEmpty(permissions, "权限不能为空");
		List<PermissionDO> permissionDOs = new ArrayList<PermissionDO>();
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
			PermissionDO permissionDO = new PermissionDO();
			BeanUtils.copyProperties(permissionDTO, permissionDO);
			permissionDOs.add(permissionDO);
		}
		return permissionDAO.batchCreate(permissionDOs);
	}
}
