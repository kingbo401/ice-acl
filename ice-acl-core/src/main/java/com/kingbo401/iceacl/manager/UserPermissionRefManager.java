package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.UserPermissionRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserPermissionRefVO;
import com.kingbo401.iceacl.model.dto.param.UserPermissionRefParam;

public interface UserPermissionRefManager {
    boolean addUserPermissionRef(UserPermissionRefParam param);
    boolean updateUserPermissionRef(UserPermissionRefParam param);
    boolean removeUserPermissionRef(UserPermissionRefParam param);
    boolean freezeUserPermissionRef(UserPermissionRefParam param);
    boolean unfreezeUserPermissionRef(UserPermissionRefParam param);
    List<UserPermissionRefVO> listUserPermissionRef(UserPermissionRefQueryParam param);
    PageVO<UserPermissionRefVO> pageUserPermissionRef(UserPermissionRefQueryParam param);
    boolean hasUserUse(long permissionId);
}
