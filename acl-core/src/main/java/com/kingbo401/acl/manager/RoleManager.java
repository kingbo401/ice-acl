package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.RoleDTO;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RoleManager {
	RoleDTO getRoleByKey(String appKey, String roleKey);
	List<RoleDTO> getRoleByKeys(String appKey, List<String> roleKeys);
	RoleDTO getRoleById(String appKey, long id);
	RoleDTO getRoleById(long id);
	List<RoleDTO> getRoleByIds(String appKey, List<Long> roleIds);
	RoleDTO saveRole(RoleDTO roleDTO);
	boolean removeRole(RoleDTO roleDTO);
	boolean freezeRole(RoleDTO roleDTO);
	boolean unfreezeRole(RoleDTO roleDTO);
	List<RoleDTO> listRole(RoleQueryParam param);
	PageVO<RoleDTO> pageRole(RoleQueryParam param);
}
