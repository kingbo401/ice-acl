package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.acl.model.dto.param.UserPermissionGroupRefParam;
import com.kingbo401.acl.model.entity.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface UserPermissionGroupRefManager {
	boolean addUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean updateUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean removeUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean freezeUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean unfreezeUserPermissionGroupRef(UserPermissionGroupRefParam param);
    boolean removeRefsByGroupId(String appKey, long groupId);
	List<Long> listUserPermissionGroupIds(UserPermissionGroupRefQueryParam param); 
	PageVO<UserPermissionGroupRefDTO> pageUserPermissionGroup(UserPermissionGroupRefQueryParam param);
	boolean hasUserUse(long groupId);
}
