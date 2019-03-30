package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.po.PermissionGroupLite;
import com.kingbo401.iceacl.model.po.param.PermissionGroupQueryParam;

import kingbo401.iceacl.common.model.PermissionGroupTreeNode;

public interface PermissionGroupManager {

    PermissionGroupDTO createPermissionGroup(PermissionGroupDTO permissionGroupDTO);

    boolean updatePermissionGroup(PermissionGroupDTO permissionGroupDTO);

    boolean removePermissionGroup(String appKey, long id);
    
    boolean freezePermissionGroup(String appKey, long id);
    
    boolean unfreezePermissionGroup(String appKey, long id);

    List<PermissionGroupTreeNode> getPermissionGroupTree(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> listPermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    PageVO<PermissionGroupDTO> pagePermissionGroup(PermissionGroupQueryParam permissionGroupQueryParam);

    List<PermissionGroupDTO> getPermissionGroupByIds(String appKey, List<Long> groupIds);
    
    List<PermissionGroupDTO> getPermissionGroupByIds(List<Long> groupIds);
    
    PermissionGroupDTO getPermissionGroupById(long id);
    
	List<PermissionGroupLite> listPermissionGroupLite(String appKey, String tenant);
}
