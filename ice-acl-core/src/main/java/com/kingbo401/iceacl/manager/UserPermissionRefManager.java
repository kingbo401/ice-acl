package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.UserPermissionRefDTO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionRefParam;
import com.kingbo401.iceacl.model.po.param.UserPermissionRefQueryParam;

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
