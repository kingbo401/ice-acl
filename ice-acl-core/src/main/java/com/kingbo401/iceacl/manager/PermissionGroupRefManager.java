package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.PermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.param.PermissionGroupRefParam;

public interface PermissionGroupRefManager {
	boolean addPermissionGroupRef(PermissionGroupRefParam param);
	
    boolean updatePermissionGroupRef(PermissionGroupRefParam param);

    boolean removePermissionGroupRef(PermissionGroupRefParam param);
    
    boolean freezePermissionGroupRef(PermissionGroupRefParam param);
    
    boolean unfreezePermissionGroupRef(PermissionGroupRefParam param);

    PageVO<PermissionDTO> pagePermission(PermissionGroupRefQueryParam param);
    
    List<PermissionDTO> listPermission(PermissionGroupRefQueryParam param);
}
