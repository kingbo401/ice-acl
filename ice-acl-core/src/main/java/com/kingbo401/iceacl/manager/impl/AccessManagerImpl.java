package com.kingbo401.iceacl.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.iceacl.dao.MenuDAO;
import com.kingbo401.iceacl.dao.MenuPermissionRefDAO;
import com.kingbo401.iceacl.dao.PermissionDAO;
import com.kingbo401.iceacl.dao.RoleDAO;
import com.kingbo401.iceacl.dao.UserRoleRefDAO;
import com.kingbo401.iceacl.manager.AccessManager;
import com.kingbo401.iceacl.model.po.PermissionPO;
import com.kingbo401.iceacl.model.po.RolePO;
import com.kingbo401.iceacl.model.po.param.CheckUserMenuParam;
import com.kingbo401.iceacl.model.po.param.CheckUserPermissionParam;

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
        String userId = param.getUserId();
        String appKey = param.getAppKey();
        String permissionKey = param.getPermissionKey();
        String tenant = param.getTenant();
        Assert.hasText(userId, "userId 不能为空");
        Assert.hasText(appKey, "appKey 不能为空");
        Assert.hasText(permissionKey,"permissionKey 不能为空");
        Assert.hasText(tenant, "tenant 不能为空");

        PermissionPO permissionPO = permissionDAO.getByKey(appKey, permissionKey);
        if(permissionPO == null){
        	return false;
        }
        Long permissionId = permissionPO.getId();
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
        RolePO rolePO = roleDAO.getRoleByKey(appKey, roleKey);
        Assert.notNull(rolePO, "角色不存在");
        int count = userRoleRefDAO.checkUserRole(userId, tenant, rolePO.getId());
        return count > 0;
	}
}
