package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.AppDO;
import com.kingbo401.iceacl.model.db.param.AppQueryParam;

public interface AppDAO {
	AppDO getByKey(@Param("appKey")String appKey);
	
	AppDO getByName(@Param("appName")String appName);
	
	AppDO getById(@Param("appId")Long appId);

	List<AppDO> getByKeys(@Param("appKeys")List<String> appKeys);

	int createApp(AppDO appDO);

	int updateApp(AppDO appDO);

	long countApp(AppQueryParam param);

	List<AppDO> pageApp(AppQueryParam param);

	List<AppDO> listApp(AppQueryParam param);
}
