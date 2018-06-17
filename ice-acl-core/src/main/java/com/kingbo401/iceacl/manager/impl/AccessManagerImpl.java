package com.kingbo401.iceacl.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.iceacl.dao.PermissionDAO;
import com.kingbo401.iceacl.dao.RoleDAO;
import com.kingbo401.iceacl.dao.UserRoleRefDAO;
import com.kingbo401.iceacl.manager.AccessManager;
import com.kingbo401.iceacl.model.db.PermissionDO;
import com.kingbo401.iceacl.model.db.RoleDO;
import com.kingbo401.iceacl.model.db.param.CheckUserPermissionParam;

@Service
public class AccessManagerImpl implements AccessManager{
	@Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserRoleRefDAO userRoleRefDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    
	@Override
	public boolean checkUserPermission(CheckUserPermissionParam param) {
		Assert.notNull(param, "参数不能为空");
        String userId = param.getUserId();
        String appKey = param.getAppKey();
        String permissionKey = param.getPermissionKey();
        String tenant = param.getTenant();
        Assert.hasText(userId, "userId 不能为空");
        Assert.hasText(appKey, "appKey 不能为空");
        Assert.hasText(permissionKey,"permissionKey 不能为空");
        Assert.hasText(tenant, "tenant 不能为空");

        PermissionDO permissionDO = permissionDAO.getByKey(appKey, permissionKey);
        if(permissionDO == null){
        	return false;
        }
        Long permissionId = permissionDO.getId();
        //1. 先校验用户-角色-权限
        int i = permissionDAO.hasUserRolePermission(userId, permissionId, tenant);
        if(i > 0) {
            return true;
        }

        //2. 在校验用户-权限
        i = permissionDAO.hasUserDirectPermission(userId, permissionId, tenant);
        if(i > 0) {
            return true;
        }

        //3. 校验用户-角色-权限组-权限
        if(param.isHierarchicalCheckRolePermissionGroup()) {
            i = permissionDAO.hasUserRolePermissionGroupPermission(userId, permissionId, tenant);
            if(i > 0) {
                return true;
            }
        }

        //4. 校验用户-权限组-权限
        if(param.isHierarchicalCheckPermissionGroup()) {
            i = permissionDAO.hasUserPermissionGroupPermission(userId, permissionId, tenant);
            if(i > 0) {
                return true;
            }
        }
        return false;
	}

	@Override
	public boolean checkUserRole(String userId, String appKey, String roleKey, String tenant) {
		Assert.notNull(userId , "userId 不能为空");
        Assert.hasText(roleKey, "roleKey 不能为空");
        Assert.hasText(appKey, "appKey 不能为空");
        Assert.hasText(tenant, "tenant 不能为空");
        RoleDO sysRoleDO = roleDAO.getRoleByKey(appKey, roleKey);
        Assert.notNull(sysRoleDO, "角色不存在");
        int count = userRoleRefDAO.hasUserRole(userId, tenant, sysRoleDO.getId());
        return count > 0;
	}
}
