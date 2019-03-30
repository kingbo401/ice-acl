package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.RolePO;
import com.kingbo401.iceacl.model.po.param.RoleQueryParam;

public interface RoleDAO {
	RolePO getRoleByKey(@Param("appKey")String appKey, @Param("roleKey")String roleKey);
	RolePO getRoleById(@Param("id")long id);
	int createRole(RolePO role);
	int updateRole(RolePO role);
	List<RolePO> listRole(RoleQueryParam param);
	List<RolePO> pageRole(RoleQueryParam param);
	long countRole(RoleQueryParam param);
	List<RolePO> getRoleByIds(@Param("ids") List<Long> ids);
	List<RolePO> getRoleByKeys(@Param("appKey")String appKey, @Param("roleKeys")List<String> roleKey);
}
