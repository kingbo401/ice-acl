package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.entity.param.PermissonQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface PermissionManager {
	PermissionDTO create(PermissionDTO permissionDTO);
	
	PermissionDTO update(PermissionDTO permissionDTO);

	int batchCreate(String appKey, List<PermissionDTO> permissions);

	boolean remove(PermissionDTO permissionDTO);

	boolean unfreeze(PermissionDTO permissionDTO);

	boolean freeze(PermissionDTO permissionDTO);

	PermissionDTO getByKey(String appKey, String permissionKey);
	
	PermissionDTO getById(String appKey, Long permissionId);
	
	List<PermissionDTO> getByKeys(String appKey, List<String> permissionKeys);
	
	List<PermissionDTO> getByIds(String appKey, List<Long> permissionIds);
	
	List<PermissionDTO> getByIds(List<Long> permissionIds);

	List<PermissionDTO> listPermission(PermissonQueryParam param);
	
	PageVO<PermissionDTO> pagePermission(PermissonQueryParam param);
}
