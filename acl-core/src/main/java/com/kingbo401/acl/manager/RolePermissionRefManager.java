package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionIdRefParam;
import com.kingbo401.acl.model.entity.param.RolePermissionRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RolePermissionRefManager {
	
    boolean addRef(RolePermissionIdRefParam param);

    boolean updateRef(RolePermissionIdRefParam param);
    
    boolean removeRef(RolePermissionIdRefParam param);
    
    boolean freezeRef(RolePermissionIdRefParam param);
    
    boolean unfreezeRef(RolePermissionIdRefParam param);
    
    List<PermissionDTO> listRolePermission(RolePermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageRolePermission(RolePermissionRefQueryParam param);
}
