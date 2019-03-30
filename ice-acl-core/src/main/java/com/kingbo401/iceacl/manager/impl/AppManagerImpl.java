package com.kingbo401.iceacl.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.kingbo401.commons.encrypt.SecurityUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.dao.AppDAO;
import com.kingbo401.iceacl.manager.AppManager;
import com.kingbo401.iceacl.model.dto.AppDTO;
import com.kingbo401.iceacl.model.po.AppPO;
import com.kingbo401.iceacl.model.po.param.AppQueryParam;

import kingbo401.iceacl.common.constant.IceAclConstant;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AppManagerImpl implements AppManager{
	@Autowired
	private AppDAO appDao;
	
	@Override
	public AppDTO getAppByKey(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		AppPO appPO = appDao.getByKey(appKey);
		if(appPO != null){
			Assert.isTrue(appPO.getStatus() == IceAclConstant.STATUS_NORMAL, "应用已被禁用");
		}
		return buildAppDTO(appPO);
	}
	
	@Override
	public AppDTO getAppById(Long appId) {
		Assert.notNull(appId, "appId不能为空");
		AppPO appPO = appDao.getById(appId);
		if(appPO != null){
			Assert.isTrue(appPO.getStatus() == IceAclConstant.STATUS_NORMAL, "应用已被禁用");
		}
		return buildAppDTO(appPO);
	}

	@Override
	public String getAppSecret(String appKey) {
		Assert.hasText(appKey, "appKey不能为空");
		AppPO appPO = appDao.getByKey(appKey);
		if(appPO == null){
			return null;
		}
		Assert.isTrue(appPO.getStatus() == IceAclConstant.STATUS_NORMAL, "应用已被禁用");
		return appPO.getAppSecret();
	}

	@Override
	public Map<String, AppDTO> getAppMap(List<String> appKeys) {
		Assert.notEmpty(appKeys, "appKeys不能为空");
		List<AppPO> apPOs = appDao.getByKeys(appKeys);
		Map<String, AppDTO> rstMap = new HashMap<String, AppDTO>();
		if(CollectionUtils.isNotEmpty(apPOs)) {
			for(AppPO appPO : apPOs) {
				rstMap.put(appPO.getAppKey(), buildAppDTO(appPO));
			}
		}
		return rstMap;
	}

	@Override
	public AppDTO createApp(AppDTO appDTO) {
		Assert.notNull(appDTO, "参数不能为空");
		String appName = appDTO.getAppName();
		String description = appDTO.getDescription();
		Assert.hasText(appName, "appName 不能为空");
		Assert.hasText(description, "description 不能为空");
		Assert.isNull(appDTO.getAppKey(), "appKey不能设置值");
		AppPO appPO = appDao.getByName(appName);
		Assert.isNull(appPO, "应用名已被使用");
		appDTO.setAppKey(SecurityUtil.getUUID());
		appDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		Date now = new Date();
		appPO.setCreateTime(now);
		appPO.setUpdateTime(now);
		appPO = new AppPO();
		BeanUtils.copyProperties(appDTO, appPO);
		appPO.setAppSecret(SecurityUtil.getUUID());
		appDao.createApp(appPO);
		return buildAppDTO(appPO);
	}

	@Override
	public boolean updateApp(AppDTO appDTO) {
		Assert.notNull(appDTO, "参数不能为空");
		String appName = appDTO.getAppName();
		String description = appDTO.getDescription();
		String appKey = appDTO.getAppKey();
		Assert.hasText(appName, "appName 不能为空");
		
		Assert.hasText(description, "description 不能为空");
		Assert.hasText(appKey, "appKey不能为空");
		AppPO appPO = appDao.getByKey(appKey);
		Assert.notNull(appPO, "应用不存在");
		if(!appName.equals(appPO.getAppName())){
			Assert.isNull(appDao.getByName(appName), "应用名已被使用");
		}
		BeanUtils.copyProperties(appDTO, appPO);
		Date now = new Date();
		appPO.setUpdateTime(now);
		appDao.updateApp(appPO);
		return true;
	}

	@Override
	public boolean freezeApp(String appKey) {
		AppPO appPO = appDao.getByKey(appKey);
		Assert.notNull(appPO, "应用不存在");
		appPO.setStatus(IceAclConstant.STATUS_FREEZE);
		appPO.setUpdateTime(new Date());
		appDao.updateApp(appPO);
		return true;
	}

	@Override
	public boolean unfreezeApp(String appKey) {
		AppPO appPO = appDao.getByKey(appKey);
		Assert.notNull(appPO, "应用不存在");
		appPO.setStatus(IceAclConstant.STATUS_NORMAL);
		appPO.setUpdateTime(new Date());
		appDao.updateApp(appPO);
		return true;
	}

	@Override
	public List<AppDTO> listApp(AppQueryParam param) {
		if(param == null){
			param = new AppQueryParam();
		}
		List<AppPO> appPOs = appDao.listApp(param);
		List<AppDTO> datas = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(appPOs)) {
			for(AppPO appPO : appPOs) {
				datas.add(buildAppDTO(appPO));
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
		
		List<AppPO> sysappPOs = appDao.pageApp(param);
		if(CollectionUtils.isNotEmpty(sysappPOs)) {
			List<AppDTO> datas = Lists.newArrayList();
			for(AppPO appPO : sysappPOs) {
				datas.add(buildAppDTO(appPO));
			}
			pageVO.setItems(datas);
		}
		return pageVO;
	}
	
	private AppDTO buildAppDTO(AppPO appPO){
		if(appPO == null) {
			return null;
		}
		AppDTO appDTO = new AppDTO();
		BeanUtils.copyProperties(appPO, appDTO);
		return appDTO;
	}
}
