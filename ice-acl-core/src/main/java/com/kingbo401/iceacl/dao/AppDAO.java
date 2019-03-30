package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.AppPO;
import com.kingbo401.iceacl.model.po.param.AppQueryParam;

public interface AppDAO {
	AppPO getByKey(@Param("appKey")String appKey);
	
	AppPO getByName(@Param("appName")String appName);
	
	AppPO getById(@Param("appId")Long appId);

	List<AppPO> getByKeys(@Param("appKeys")List<String> appKeys);

	int createApp(AppPO app);

	int updateApp(AppPO app);

	long countApp(AppQueryParam param);

	List<AppPO> pageApp(AppQueryParam param);

	List<AppPO> listApp(AppQueryParam param);
}
