package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.PermissionGroupTreeNode;
import com.kingbo401.acl.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.model.entity.PermissionGroupLite;
import com.kingbo401.acl.model.entity.param.PermissionGroupQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface PermissionGroupManager {

    PermissionGroupDTO create(PermissionGroupDTO permissionGroupDTO);

    boolean update(PermissionGroupDTO permissionGroupDTO);

    boolean remove(PermissionGroupDTO permissionGroupDTO);
    
    boolean freeze(PermissionGroupDTO permissionGroupDTO);
    
    boolean unfreeze(PermissionGroupDTO permissionGroupDTO);

    List<PermissionGroupTreeNode> getPermissionGroupTree(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> listPermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    PageVO<PermissionGroupDTO> pagePermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> getByIds(String appKey, List<Long> groupIds);
    
    List<PermissionGroupDTO> getByIds(List<Long> groupIds);
    
    PermissionGroupDTO getById(long id);
    
	List<PermissionGroupLite> listPermissionGroupLite(String appKey, String tenant);
}
