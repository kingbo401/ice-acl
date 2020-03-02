package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.PermissionDTO;
import com.kingbo401.acl.common.model.dto.param.MenuPermissionRefParam;
import com.kingbo401.acl.model.entity.param.MenuPermissionRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface MenuPermissionRefManager {
	boolean addRef(MenuPermissionRefParam param);

    boolean updateRef(MenuPermissionRefParam param);
    
    boolean removeRef(MenuPermissionRefParam param);
    
    boolean freezeRef(MenuPermissionRefParam param);
    
    boolean unfreezeRef(MenuPermissionRefParam param);
    
    List<PermissionDTO> listMenuPermission(MenuPermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageMenuPermission(MenuPermissionRefQueryParam param);
}
