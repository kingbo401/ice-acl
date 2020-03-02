package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.RoleDTO;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RoleManager {
	RoleDTO getByKey(String appKey, String roleKey);
	List<RoleDTO> getByKeys(String appKey, List<String> roleKeys);
	RoleDTO getById(String appKey, long id);
	RoleDTO getById(long id);
	List<RoleDTO> getByIds(String appKey, List<Long> roleIds);
	RoleDTO save(RoleDTO roleDTO);
	boolean remove(RoleDTO roleDTO);
	boolean freeze(RoleDTO roleDTO);
	boolean unfreeze(RoleDTO roleDTO);
	List<RoleDTO> listRole(RoleQueryParam param);
	PageVO<RoleDTO> pageRole(RoleQueryParam param);
}
