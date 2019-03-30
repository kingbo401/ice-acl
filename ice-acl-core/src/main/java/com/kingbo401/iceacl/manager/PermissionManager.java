package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.po.param.PermissonQueryParam;

public interface PermissionManager {
	PermissionDTO createPermission(PermissionDTO permissionDTO);
	
	PermissionDTO updatePermission(PermissionDTO permissionDTO);

	int createPermissions(String appKey, List<PermissionDTO> permissions);

	boolean removePermission(String appKey, Long id);

	boolean unfreezePermission(String appKey, Long id);

	boolean freezePermission(String appKey, Long id);

	PermissionDTO getPermissionByKey(String appKey, String permissionKey);
	
	PermissionDTO getPermissionById(String appKey, Long permissionId);
	
	List<PermissionDTO> getPermissionByKeys(String appKey, List<String> permissionKeys);
	
	List<PermissionDTO> getPermissionByIds(String appKey, List<Long> permissionIds);
	
	List<PermissionDTO> getPermissionByIds(List<Long> permissionIds);

	List<PermissionDTO> listPermission(PermissonQueryParam param);
	
	PageVO<PermissionDTO> pagePermission(PermissonQueryParam param);
}
