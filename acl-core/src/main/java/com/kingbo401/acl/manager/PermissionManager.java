package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.entity.param.PermissonQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface PermissionManager {
	PermissionDTO createPermission(PermissionDTO permissionDTO);
	
	PermissionDTO updatePermission(PermissionDTO permissionDTO);

	int createPermissions(String appKey, List<PermissionDTO> permissions);

	boolean removePermission(PermissionDTO permissionDTO);

	boolean unfreezePermission(PermissionDTO permissionDTO);

	boolean freezePermission(PermissionDTO permissionDTO);

	PermissionDTO getPermissionByKey(String appKey, String permissionKey);
	
	PermissionDTO getPermissionById(String appKey, Long permissionId);
	
	List<PermissionDTO> getPermissionByKeys(String appKey, List<String> permissionKeys);
	
	List<PermissionDTO> getPermissionByIds(String appKey, List<Long> permissionIds);
	
	List<PermissionDTO> getPermissionByIds(List<Long> permissionIds);

	List<PermissionDTO> listPermission(PermissonQueryParam param);
	
	PageVO<PermissionDTO> pagePermission(PermissonQueryParam param);
}
