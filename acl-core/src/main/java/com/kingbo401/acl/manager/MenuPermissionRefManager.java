package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.PermissionDTO;
import com.kingbo401.acl.model.dto.param.MenuPermissionRefParam;
import com.kingbo401.acl.model.entity.param.MenuPermissionRefQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface MenuPermissionRefManager {
	boolean addMenuPermissionRef(MenuPermissionRefParam param);

    boolean updateMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean removeMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean freezeMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean unfreezeMenuPermissionRef(MenuPermissionRefParam param);
    
    List<PermissionDTO> listMenuPermission(MenuPermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageMenuPermission(MenuPermissionRefQueryParam param);
}
