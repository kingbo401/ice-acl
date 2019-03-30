package com.kingbo401.iceacl.manager;

import java.util.List;
import java.util.Map;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.AppDTO;
import com.kingbo401.iceacl.model.po.param.AppQueryParam;

public interface AppManager {
	AppDTO getAppByKey(String appKey);
	
	AppDTO getAppById(Long appId);
	
	Map<String, AppDTO> getAppMap(List<String> appKeys);
	
	String getAppSecret(String appKey);

	AppDTO createApp(AppDTO appDTO);

	boolean updateApp(AppDTO appDTO);

	boolean freezeApp(String appKey);

	boolean unfreezeApp(String appKey);

	List<AppDTO> listApp(AppQueryParam param);

	PageVO<AppDTO> pageApp(AppQueryParam param);
}
