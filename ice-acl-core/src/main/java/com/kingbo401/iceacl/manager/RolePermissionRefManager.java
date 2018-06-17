package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.RolePermissionRefQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.param.RolePermissionIdRefParam;

public interface RolePermissionRefManager {
	
    boolean addRolePermissionRef(RolePermissionIdRefParam param);

    boolean updateRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean removeRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean freezeRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean unfreezeRolePermissionRef(RolePermissionIdRefParam param);
    
    List<PermissionDTO> listRolePermission(RolePermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageRolePermission(RolePermissionRefQueryParam param);
}
