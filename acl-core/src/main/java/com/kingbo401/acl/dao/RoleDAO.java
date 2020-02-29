package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.RoleDO;
import com.kingbo401.acl.model.entity.param.RoleQueryParam;

public interface RoleDAO {
	RoleDO getByKey(@Param("appKey")String appKey, @Param("roleKey")String roleKey);
	RoleDO getByKey0(@Param("appKey")String appKey, @Param("roleKey")String roleKey);
	RoleDO getById(@Param("id")long id);
	int create(RoleDO role);
	int update(RoleDO role);
	List<RoleDO> listRole(RoleQueryParam param);
	List<RoleDO> pageRole(RoleQueryParam param);
	long countRole(RoleQueryParam param);
	List<RoleDO> getByIds(@Param("ids") List<Long> ids);
	List<RoleDO> getByKeys(@Param("appKey")String appKey, @Param("roleKeys")List<String> roleKey);
}
