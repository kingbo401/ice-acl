package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.UserPermissionGroupRefDTO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionGroupRefParam;
import com.kingbo401.iceacl.model.po.param.UserPermissionGroupRefQueryParam;

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
