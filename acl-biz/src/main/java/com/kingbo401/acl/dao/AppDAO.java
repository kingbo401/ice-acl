package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.AppDO;
import com.kingbo401.acl.model.entity.param.AppQueryParam;

public interface AppDAO {
	AppDO getByKey(@Param("appKey")String appKey);
	
	AppDO getByName(@Param("name")String name);
	
	AppDO getById(@Param("appId")Long appId);

	List<AppDO> getByKeys(@Param("appKeys")List<String> appKeys);

	int createApp(AppDO app);

	int updateApp(AppDO app);

	long countApp(AppQueryParam param);

	List<AppDO> pageApp(AppQueryParam param);

	List<AppDO> listApp(AppQueryParam param);
}
