package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserPermissionGroupRefVO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionGroupRefParam;

public interface UserPermissionGroupRefManager {
	boolean addUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean updateUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean removeUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean freezeUserPermissionGroupRef(UserPermissionGroupRefParam param);
	boolean unfreezeUserPermissionGroupRef(UserPermissionGroupRefParam param);
    boolean removeRefsByGroupId(String appKey, long groupId);
	List<Long> listUserPermissionGroupIds(UserPermissionGroupRefQueryParam param); 
	PageVO<UserPermissionGroupRefVO> pageUserPermissionGroup(UserPermissionGroupRefQueryParam param);
	boolean hasUserUse(long groupId);
}
