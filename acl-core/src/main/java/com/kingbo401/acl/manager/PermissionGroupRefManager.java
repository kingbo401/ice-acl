package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.param.PermissionGroupRefParam;
import com.kingbo401.acl.model.entity.param.PermissionGroupRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface PermissionGroupRefManager {
	boolean addPermissionGroupRef(PermissionGroupRefParam param);
	
    boolean updatePermissionGroupRef(PermissionGroupRefParam param);

    boolean removePermissionGroupRef(PermissionGroupRefParam param);
    
    boolean freezePermissionGroupRef(PermissionGroupRefParam param);
    
    boolean unfreezePermissionGroupRef(PermissionGroupRefParam param);

    PageVO<PermissionDTO> pagePermission(PermissionGroupRefQueryParam param);
    
    List<PermissionDTO> listPermission(PermissionGroupRefQueryParam param);
}
