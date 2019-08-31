package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.UserPermissionRefDTO;
import com.kingbo401.acl.model.dto.param.UserPermissionRefParam;
import com.kingbo401.acl.model.entity.param.UserPermissionRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface UserPermissionRefManager {
    boolean addUserPermissionRef(UserPermissionRefParam param);
    boolean updateUserPermissionRef(UserPermissionRefParam param);
    boolean removeUserPermissionRef(UserPermissionRefParam param);
    boolean freezeUserPermissionRef(UserPermissionRefParam param);
    boolean unfreezeUserPermissionRef(UserPermissionRefParam param);
    List<UserPermissionRefDTO> listUserPermissionRef(UserPermissionRefQueryParam param);
    PageVO<UserPermissionRefDTO> pageUserPermissionRef(UserPermissionRefQueryParam param);
    boolean hasUserUse(long permissionId);
}
