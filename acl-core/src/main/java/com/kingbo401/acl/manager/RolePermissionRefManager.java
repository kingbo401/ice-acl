package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionIdRefParam;
import com.kingbo401.acl.model.entity.param.RolePermissionRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RolePermissionRefManager {
	
    boolean addRolePermissionRef(RolePermissionIdRefParam param);

    boolean updateRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean removeRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean freezeRolePermissionRef(RolePermissionIdRefParam param);
    
    boolean unfreezeRolePermissionRef(RolePermissionIdRefParam param);
    
    List<PermissionDTO> listRolePermission(RolePermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageRolePermission(RolePermissionRefQueryParam param);
}
