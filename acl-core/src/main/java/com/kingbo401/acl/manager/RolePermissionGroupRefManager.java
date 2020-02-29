package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.dto.param.RolePermissionGroupRefParam;
import com.kingbo401.acl.model.entity.param.RolePermissionGroupRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface RolePermissionGroupRefManager {

    boolean addRef(RolePermissionGroupRefParam param);
    
    boolean updateRef(RolePermissionGroupRefParam param);

    boolean removeRef(RolePermissionGroupRefParam param);
    
    boolean freezeRef(RolePermissionGroupRefParam param);
    
    boolean unfreezeRef(RolePermissionGroupRefParam param);

    List<PermissionGroupDTO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    PageVO<PermissionGroupDTO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<Long> listUserRolePermissionGroupIds(String userId, String appKey, String tenant);
}
