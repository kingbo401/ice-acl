package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionGroupRefParam;
import com.kingbo401.acl.model.entity.param.RolePermissionGroupRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RolePermissionGroupRefManager {

    boolean addRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean updateRolePermissionGroupRef(RolePermissionGroupRefParam param);

    boolean removeRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean freezeRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean unfreezeRolePermissionGroupRef(RolePermissionGroupRefParam param);

    List<PermissionGroupDTO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    PageVO<PermissionGroupDTO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<Long> listUserRolePermissionGroupIds(Long userId, String appKey, String tenant);
}
