package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.param.RolePermissionGroupRefParam;
import com.kingbo401.iceacl.model.po.param.RolePermissionGroupRefQueryParam;

public interface RolePermissionGroupRefManager {

    boolean addRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean updateRolePermissionGroupRef(RolePermissionGroupRefParam param);

    boolean removeRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean freezeRolePermissionGroupRef(RolePermissionGroupRefParam param);
    
    boolean unfreezeRolePermissionGroupRef(RolePermissionGroupRefParam param);

    List<PermissionGroupDTO> listPermissionGroup(RolePermissionGroupRefQueryParam param);
    
    PageVO<PermissionGroupDTO> pagePermissionGroup(RolePermissionGroupRefQueryParam param);
    
    List<Long> listUserRolePermissionGroupIds(String userId, String appKey, String tenant);
}
