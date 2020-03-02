package com.kingbo401.acl.manager;

import java.util.List;
import java.util.Map;

import com.kingbo401.acl.common.model.dto.AppDTO;
import com.kingbo401.acl.model.entity.param.AppQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface AppManager {
	AppDTO getByKey(String appKey);
	
	AppDTO getById(Long appId);
	
	Map<String, AppDTO> getAppMap(List<String> appKeys);
	
	String getSecret(String appKey);

	AppDTO create(AppDTO appDTO);

	boolean update(AppDTO appDTO);

	boolean freeze(AppDTO appDTO);

	boolean unfreeze(AppDTO appDTO);

	List<AppDTO> listApp(AppQueryParam param);

	PageVO<AppDTO> pageApp(AppQueryParam param);
}
