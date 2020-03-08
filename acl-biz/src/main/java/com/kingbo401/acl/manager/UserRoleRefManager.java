package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.common.model.dto.UserRoleRefsDTO;
import com.kingbo401.acl.common.model.dto.param.UserRoleRefParam;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.model.entity.param.UsersRoleRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface UserRoleRefManager {

	PageVO<UserRoleRefsDTO> pageUserRoleRefs(UsersRoleRefQueryParam usersRoleRefQueryParam);

	List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	PageVO<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	boolean addRef(UserRoleRefParam param);

	boolean updateRef(UserRoleRefParam param);

	boolean removeRef(UserRoleRefParam param);
	
	boolean freezeRef(UserRoleRefParam param);
	
	boolean unfreezeRef(UserRoleRefParam param);

	List<String> listUserTenant(String userId, String appKey, List<String> roleKeys);
	
	boolean hasUserUse(long roleId);
}
