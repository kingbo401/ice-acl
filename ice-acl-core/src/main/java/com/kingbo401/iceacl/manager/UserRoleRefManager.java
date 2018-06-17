package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.param.UsersRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;
import com.kingbo401.iceacl.model.dto.UserRoleRefsDTO;
import com.kingbo401.iceacl.model.dto.param.UserRoleRefParam;

public interface UserRoleRefManager {

	PageVO<UserRoleRefsDTO> pageUserRoleRefs(UsersRoleRefQueryParam usersRoleRefQueryParam);

	List<UserRoleRefVO> listUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	PageVO<UserRoleRefVO> pageUserRoleRef(UserRoleRefQueryParam userRoleRefQueryParam);

	boolean addUserRoleRef(UserRoleRefParam param);

	boolean updateUserRoleRef(UserRoleRefParam param);

	boolean removeUserRoleRef(UserRoleRefParam param);
	
	boolean freezeUserRoleRef(UserRoleRefParam param);
	
	boolean unfreezeUserRoleRef(UserRoleRefParam param);

	List<String> listUserTenant(String userId, String appKey, List<String> roleKeys);
	
	boolean hasUserUse(long roleId);
}
