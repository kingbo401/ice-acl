package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.PermissionDTO;
import com.kingbo401.acl.common.model.dto.param.PermissionGroupRefParam;
import com.kingbo401.acl.model.entity.param.PermissionGroupRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface PermissionGroupRefManager {
	boolean addRef(PermissionGroupRefParam param);
	
    boolean updateRef(PermissionGroupRefParam param);

    boolean removeRef(PermissionGroupRefParam param);
    
    boolean freezeRef(PermissionGroupRefParam param);
    
    boolean unfreezeRef(PermissionGroupRefParam param);

    PageVO<PermissionDTO> pagePermission(PermissionGroupRefQueryParam param);
    
    List<PermissionDTO> listPermission(PermissionGroupRefQueryParam param);
}
