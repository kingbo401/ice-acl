package com.kingbo401.acl.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.kingbo401.acl.dao.AppDAO;
import com.kingbo401.acl.manager.AppManager;
import com.kingbo401.acl.model.dto.AppDTO;
import com.kingbo401.acl.model.entity.AppDO;
import com.kingbo401.acl.model.entity.param.AppQueryParam;
import com.kingbo401.commons.encrypt.SecurityUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class AppManagerImpl implements AppManager{
	@Autowired
	private AppDAO appDao;
	
	@Override
	public AppDTO getAppByKey(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		AppDO appDO = appDao.getByKey(appKey);
		if(appDO != null){
			Assert.isTrue(appDO.getStatus() == AclConstant.STATUS_NORMAL, "应用已被禁用");
		}
		return buildAppDTO(appDO);
	}
	
	@Override
	public AppDTO getAppById(Long appId) {
		Assert.notNull(appId, "appId不能为空");
		AppDO appDO = appDao.getById(appId);
		if(appDO != null){
			Assert.isTrue(appDO.getStatus() == AclConstant.STATUS_NORMAL, "应用已被禁用");
		}
		return buildAppDTO(appDO);
	}

	@Override
	public String getAppSecret(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		AppDO appDO = appDao.getByKey(appKey);
		if(appDO == null){
			return null;
		}
		Assert.isTrue(appDO.getStatus() == AclConstant.STATUS_NORMAL, "应用已被禁用");
		return appDO.getAppSecret();
	}

	@Override
	public Map<String, AppDTO> getAppMap(List<String> appKeys) {
		Assert.notEmpty(appKeys, "appKeys不能为空");
		List<AppDO> apDOs = appDao.getByKeys(appKeys);
		Map<String, AppDTO> rstMap = new HashMap<String, AppDTO>();
		if(CollectionUtils.isNotEmpty(apDOs)) {
			for(AppDO appDO : apDOs) {
				rstMap.put(appDO.getAppKey(), buildAppDTO(appDO));
			}
		}
		return rstMap;
	}

	@Override
	public AppDTO createApp(AppDTO appDTO) {
		Assert.notNull(appDTO, "参数不能为空");
		String appName = appDTO.getName();
		String description = appDTO.getDescription();
		Assert.hasText(appName, "appName 不能为空");
		Assert.hasText(description, "description 不能为空");
		Assert.isNull(appDTO.getAppKey(), "appKey不能设置值");
		AppDO appDO = appDao.getByName(appName);
		Assert.isNull(appDO, "应用名已被使用");
		appDTO.setAppKey(SecurityUtil.getUUID());
		appDTO.setStatus(AclConstant.STATUS_NORMAL);
		appDO = new AppDO();
		BeanUtils.copyProperties(appDTO, appDO);
		appDO.setAppSecret(SecurityUtil.getUUID());
		appDao.createApp(appDO);
		return buildAppDTO(appDO);
	}

	@Override
	public boolean updateApp(AppDTO appDTO) {
		Assert.notNull(appDTO, "参数不能为空");
		Long appId = appDTO.getId();
		String appKey = appDTO.getAppKey();
		String appName = appDTO.getName();
		String description = appDTO.getDescription();
		Assert.notNull(appId, "appId不能为空");
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(appName, "appName 不能为空");
		Assert.hasText(description, "description 不能为空");
		AppDO appDO = appDao.getById(appId);
		Assert.notNull(appDO, "应用不存在");
		Assert.isTrue(appKey.equals(appDO.getAppKey()), "appKey不能修改");
		if(!appName.equals(appDO.getName())){
			Assert.isNull(appDao.getByName(appName), "应用名已被使用");
		}
		BeanUtils.copyProperties(appDTO, appDO);
		appDao.updateApp(appDO);
		return true;
	}

	@Override
	public boolean freezeApp(AppDTO appDTO) {
		String appKey = appDTO.getAppKey();
		AppDO appDO = appDao.getByKey(appKey);
		Assert.notNull(appDO, "应用不存在");
		appDO.setStatus(AclConstant.STATUS_FREEZE);
		appDao.updateApp(appDO);
		return true;
	}

	@Override
	public boolean unfreezeApp(AppDTO appDTO) {
		String appKey = appDTO.getAppKey();
		AppDO appDO = appDao.getByKey(appKey);
		Assert.notNull(appDO, "应用不存在");
		appDO.setStatus(AclConstant.STATUS_NORMAL);
		appDao.updateApp(appDO);
		return true;
	}

	@Override
	public List<AppDTO> listApp(AppQueryParam param) {
		if(param == null){
			param = new AppQueryParam();
		}
		List<AppDO> appDOs = appDao.listApp(param);
		List<AppDTO> datas = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(appDOs)) {
			for(AppDO appDO : appDOs) {
				datas.add(buildAppDTO(appDO));
			}
		}
		return datas;
	}

	@Override
	public PageVO<AppDTO> pageApp(AppQueryParam param) {
		if(param == null){
			param = new AppQueryParam();
			param.setPageNum(1);
			param.setPageSize(10);
		}
		PageVO<AppDTO> pageVO = new PageVO<AppDTO>(param);
		if(param.isReturnTotalCount()){
			long total = appDao.countApp(param);
			pageVO.setTotal(total);
			if(total == 0) {
				return pageVO;
			}
		}
		
		List<AppDO> sysappDOs = appDao.pageApp(param);
		if(CollectionUtils.isNotEmpty(sysappDOs)) {
			List<AppDTO> datas = Lists.newArrayList();
			for(AppDO appDO : sysappDOs) {
				datas.add(buildAppDTO(appDO));
			}
			pageVO.setItems(datas);
		}
		return pageVO;
	}
	
	private AppDTO buildAppDTO(AppDO appDO){
		if(appDO == null) {
			return null;
		}
		AppDTO appDTO = new AppDTO();
		BeanUtils.copyProperties(appDO, appDTO);
		return appDTO;
	}
}
