package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.db.param.MenuPermissionRefQueryParam;
import com.kingbo401.iceacl.model.dto.PermissionDTO;
import com.kingbo401.iceacl.model.dto.param.MenuPermissionRefParam;

public interface MenuPermissionRefManager {
	boolean addMenuPermissionRef(MenuPermissionRefParam param);

    boolean updateMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean removeMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean freezeMenuPermissionRef(MenuPermissionRefParam param);
    
    boolean unfreezeMenuPermissionRef(MenuPermissionRefParam param);
    
    List<PermissionDTO> listMenuPermission(MenuPermissionRefQueryParam param);
    
    PageVO<PermissionDTO> pageMenuPermission(MenuPermissionRefQueryParam param);
}
