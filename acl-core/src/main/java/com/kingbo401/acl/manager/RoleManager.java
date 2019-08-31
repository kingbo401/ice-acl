package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.RoleDTO;
import com.kingbo401.acl.model.dto.param.RoleMenuIdRefParam;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RoleManager {
	RoleDTO getRoleByKey(String appKey, String roleKey);
	List<RoleDTO> getRoleByKeys(String appKey, List<String> roleKeys);
	RoleDTO getRoleById(String appKey, long id);
	RoleDTO getRoleById(long id);
	List<RoleDTO> getRoleByIds(String appKey, List<Long> roleIds);
	RoleDTO createRole(RoleDTO roleDTO);
	RoleDTO updateRole(RoleDTO roleDTO);
	boolean removeRole(String appKey, long roleId);
	boolean freezeRole(String appKey, long roleId);
	boolean unfreezeRole(String appKey, long roleId);
	List<RoleDTO> listRole(RoleQueryParam param);
	PageVO<RoleDTO> pageRole(RoleQueryParam param);
    boolean updateRoleMenuRef(RoleMenuIdRefParam param);
}
