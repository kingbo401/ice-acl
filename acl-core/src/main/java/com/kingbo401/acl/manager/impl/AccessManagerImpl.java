package com.kingbo401.acl.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.MenuDAO;
import com.kingbo401.acl.dao.MenuPermissionRefDAO;
import com.kingbo401.acl.dao.PermissionDAO;
import com.kingbo401.acl.dao.RoleDAO;
import com.kingbo401.acl.dao.UserRoleRefDAO;
import com.kingbo401.acl.manager.AccessManager;
import com.kingbo401.acl.model.entity.PermissionDO;
import com.kingbo401.acl.model.entity.RoleDO;
import com.kingbo401.acl.model.entity.param.CheckUserMenuParam;
import com.kingbo401.acl.model.entity.param.CheckUserPermissionParam;

@Service
public class AccessManagerImpl implements AccessManager{
	@Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserRoleRefDAO userRoleRefDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private MenuDAO menuDAO;
    @Autowired
    private MenuPermissionRefDAO menuPermissionRefDAO;
    
	@Override
	public boolean checkUserPermission(CheckUserPermissionParam param) {
		Assert.notNull(param, "参数不能为空");
		Long userId = param.getUserId();
        String appKey = param.getAppKey();
        String permissionKey = param.getPermissionKey();
        String tenant = param.getTenant();
        Assert.notNull(userId, "userId 不能为空");
        Assert.hasText(appKey, "appKey 不能为空");
        Assert.hasText(permissionKey,"permissionKey 不能为空");
        Assert.hasText(tenant, "tenant 不能为空");

        PermissionDO permissionDO = permissionDAO.getByKey(appKey, permissionKey);
        if(permissionDO == null){
        	return false;
        }
        Long permissionId = permissionDO.getId();
        //查出权限关联的菜单，用户拥有菜单，就认为拥有此权限
        if(param.isHierarchicalObtainMenuPermission()){
        	List<Long> menuIds = menuPermissionRefDAO.listMenuIds(permissionId);
        	CheckUserMenuParam checkUserMenuParam = new CheckUserMenuParam();
        	checkUserMenuParam.setAppKey(appKey);
        	checkUserMenuParam.setUserId(userId);
        	checkUserMenuParam.setTenant(tenant);
        	checkUserMenuParam.setMenuIds(menuIds);
        	if(menuDAO.checkUserMenu(checkUserMenuParam) > 0){
        		return true;
        	}
        }
        //1 校验用户-权限
        int i = permissionDAO.checkUserDirectPermission(userId, permissionId, tenant);
        if(i > 0) {
            return true;
        }
        
        //2 校验用户-角色-权限
        if(param.isHierarchicalCheckRole()){
	        i = permissionDAO.checkUserRolePermission(userId, permissionId, tenant);
	        if(i > 0) {
	            return true;
	        }
        }
        
        //3 校验用户-角色-权限组-权限
        if(param.isHierarchicalCheckRolePermissionGroup()) {
            i = permissionDAO.checkUserRolePermissionGroupPermission(userId, permissionId, tenant);
            if(i > 0) {
                return true;
            }
        }

        //4 校验用户-权限组-权限
        if(param.isHierarchicalCheckPermissionGroup()) {
            i = permissionDAO.checkUserPermissionGroupPermission(userId, permissionId, tenant);
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
        RoleDO roleDO = roleDAO.getByKey(appKey, roleKey);
        Assert.notNull(roleDO, "角色不存在");
        int count = userRoleRefDAO.checkUserRole(userId, tenant, roleDO.getId());
        return count > 0;
	}
}
