package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.model.dto.UserRoleRefsDTO;
import com.kingbo401.acl.model.dto.param.UserRoleRefParam;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.model.entity.param.UsersRoleRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface UserRoleRefManager {

	PageVO<UserRoleRefsDTO> pageUserRoleRefs(UsersRoleRefQueryParam usersRoleRefQueryParam);

	List<UserRoleRefDTO> listUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	PageVO<UserRoleRefDTO> pageUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	boolean addUserRoleRef(UserRoleRefParam param);

	boolean updateUserRoleRef(UserRoleRefParam param);

	boolean removeUserRoleRef(UserRoleRefParam param);
	
	boolean freezeUserRoleRef(UserRoleRefParam param);
	
	boolean unfreezeUserRoleRef(UserRoleRefParam param);

	List<String> listUserTenant(Long userId, String appKey, List<String> roleKeys);
	
	boolean hasUserUse(long roleId);
}
