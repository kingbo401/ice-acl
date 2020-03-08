package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.enums.GrantTargetType;
import com.kingbo401.acl.common.model.dto.DataModelDTO;
import com.kingbo401.acl.common.model.dto.DataPropertyAccessDTO;
import com.kingbo401.acl.common.model.dto.DataPropertyDTO;
import com.kingbo401.acl.common.model.dto.param.DataPropertyCodeAccessParam;
import com.kingbo401.acl.dao.DataPropertyAccessDAO;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataPropertyAccessManager;
import com.kingbo401.acl.manager.DataPropertyManager;
import com.kingbo401.acl.model.entity.DataPropertyAccessDO;
import com.kingbo401.acl.model.entity.param.DataPropertyAccessParam;
import com.kingbo401.commons.util.CollectionUtil;

@Service
public class DataPropertyAccessManagerImpl implements DataPropertyAccessManager{
	@Autowired
	private DataPropertyAccessDAO dataPropertyAccessDAO;
	@Autowired
	private DataModelManager modelManager;
	@Autowired
	private DataPropertyManager propertyManager;
	
	private boolean isAccessTypeValid(Integer controlType){
		if(controlType == null){
			return false;
		}
		if(controlType == AclConstant.DATA_PROPERTY_ACCESS_ALLOW || controlType == AclConstant.DATA_PROPERTY_ACCESS_FORBIDDEN){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean create(DataPropertyCodeAccessParam param) {
		Assert.isTrue(isAccessTypeValid(param.getAccessType()), "访问类型不合法，请配置合法值；0禁止访问 1允许访问");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String modelCode = param.getModelCode();
		List<String> propertyCodes = param.getPropertyCodes();
		String grantTargetId = param.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId 不能为空");
		String tenant = param.getTenant();
		Assert.hasText(tenant, "tenant 不能为空");
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		int grantTargetType = param.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		// 校验模型是否存在
		DataModelDTO modelDTO = modelManager.getByCode(modelCode);
		Assert.notNull(modelDTO, "modelCode 不存在");
		
		// 校验属性是否全部存在
		List<DataPropertyDTO> dataProperties = propertyManager.getByCodes(modelDTO.getId(), propertyCodes);
		Assert.notEmpty(dataProperties, "属性不存在");
		List<DataPropertyAccessDO> dataPropertyAccessDOs = new ArrayList<DataPropertyAccessDO>();
		for (DataPropertyDTO propertyDTO : dataProperties) {
			DataPropertyAccessDO dataPropertyAccessDO = new DataPropertyAccessDO();
			dataPropertyAccessDO.setAppKey(appKey);
			dataPropertyAccessDO.setAccessType(param.getAccessType());
			dataPropertyAccessDO.setGrantTargetId(grantTargetId);
			dataPropertyAccessDO.setGrantTargetType(grantTargetType);
			dataPropertyAccessDO.setModelId(modelDTO.getId());
			dataPropertyAccessDO.setPropertyId(propertyDTO.getId());
			dataPropertyAccessDO.setTenant(tenant);
			dataPropertyAccessDO.setStatus(AclConstant.STATUS_NORMAL);
			dataPropertyAccessDOs.add(dataPropertyAccessDO);
		}
		dataPropertyAccessDAO.batchCreate(dataPropertyAccessDOs);
		return true;
	}

	@Override
	public boolean update(DataPropertyCodeAccessParam param) {
		Assert.isTrue(isAccessTypeValid(param.getAccessType()), "访问类型不合法，请配置合法值；0禁止访问 1允许访问");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String modelCode = param.getModelCode();
		List<String> propertyCodes = param.getPropertyCodes();
		String grantTargetId = param.getGrantTargetId();
		Assert.hasText(grantTargetId, "targetId 不能为空");
		String tenant = param.getTenant();
		Assert.hasText(tenant, "tenant 不能为空");
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		int grantTargetType = param.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		// 校验模型是否存在
		DataModelDTO modelDTO = modelManager.getByCode(modelCode);
		Assert.notNull(modelDTO, "modelCode 不存在");
		
		// 校验属性是否全部存在
		List<DataPropertyDTO> dataProperties = propertyManager.getByCodes(modelDTO.getId(), propertyCodes);
		Assert.notEmpty(dataProperties, "属性不存在");
		
		//删除老数据
		DataPropertyAccessParam dataPropertyAccessParam = new DataPropertyAccessParam();
		BeanUtils.copyProperties(param, dataPropertyAccessParam);
		dataPropertyAccessParam.setPropertyIds(null);
		dataPropertyAccessParam.setStatus(AclConstant.STATUS_REMOVE);
		dataPropertyAccessDAO.updateRefsStatus(dataPropertyAccessParam);
		
		List<DataPropertyAccessDO> dataPropertyAccessDOs = new ArrayList<DataPropertyAccessDO>();
		for (DataPropertyDTO propertyDTO : dataProperties) {
			DataPropertyAccessDO dataPropertyAccessDO = new DataPropertyAccessDO();
			dataPropertyAccessDO.setAppKey(appKey);
			dataPropertyAccessDO.setAccessType(param.getAccessType());
			dataPropertyAccessDO.setGrantTargetId(grantTargetId);
			dataPropertyAccessDO.setGrantTargetType(grantTargetType);
			dataPropertyAccessDO.setModelId(modelDTO.getId());
			dataPropertyAccessDO.setPropertyId(propertyDTO.getId());
			dataPropertyAccessDO.setTenant(tenant);
			dataPropertyAccessDO.setStatus(AclConstant.STATUS_NORMAL);
			dataPropertyAccessDOs.add(dataPropertyAccessDO);
		}
		dataPropertyAccessDAO.batchCreate(dataPropertyAccessDOs);
		return true;
	}
	
	private boolean updateDataPropertyAccessStatus(DataPropertyCodeAccessParam param, int status){
		Assert.isTrue(isAccessTypeValid(param.getAccessType()), "控制类型不合法，请配置合法值；0禁止访问 1允许访问");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String modelCode = param.getModelCode();
		List<String> propertyCodes = param.getPropertyCodes();
		String grantTargetId = param.getGrantTargetId();
		Assert.hasText(grantTargetId, "targetId 不能为空");
		String tenant = param.getTenant();
		Assert.hasText(tenant, "tenant 不能为空");
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		int grantTargetType = param.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		// 校验模型是否存在
		DataModelDTO modelDTO = modelManager.getByCode(modelCode);
		Assert.notNull(modelDTO, "modelCode 不存在");
		
		// 校验属性是否全部存在
		List<DataPropertyDTO> dataProperties = propertyManager.getByCodes(modelDTO.getId(), propertyCodes);
		Assert.notEmpty(dataProperties, "属性不存在");
		List<Long> propertyIds = new ArrayList<Long>();
		for(DataPropertyDTO dataPropertyDTO : dataProperties){
			propertyIds.add(dataPropertyDTO.getId());
		}
		//删除老数据
		DataPropertyAccessParam dataPropertyControlParam = new DataPropertyAccessParam();
		BeanUtils.copyProperties(param, dataPropertyControlParam);
		dataPropertyControlParam.setPropertyIds(propertyIds);
		dataPropertyControlParam.setStatus(status);
		dataPropertyAccessDAO.updateRefsStatus(dataPropertyControlParam);
		return true;
	}

	@Override
	public boolean remove(DataPropertyCodeAccessParam param) {
		return updateDataPropertyAccessStatus(param, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freeze(DataPropertyCodeAccessParam param) {
		return updateDataPropertyAccessStatus(param, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreeze(DataPropertyCodeAccessParam param) {
		return updateDataPropertyAccessStatus(param, AclConstant.STATUS_NORMAL);
	}

	@Override
	public List<DataPropertyAccessDTO> listDataPropertyAccess(DataPropertyCodeAccessParam param) {
		Assert.hasText(param.getGrantTargetId(), "targetId 不能为空");
		Assert.hasText(param.getTenant(), "tenant 不能为空");
		String appKey = param.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		int grantTargetType = param.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		String modelCode = param.getModelCode();
		Assert.hasText(modelCode, "modelCode 不能为空");
		
		DataModelDTO modelDTO = modelManager.getByCode(modelCode);
		Assert.notNull(modelDTO, "modelCode 不存在");
		
		List<DataPropertyDTO> dataPropertyDTOs = propertyManager.listDataProperty(modelDTO.getId());
		Assert.notEmpty(dataPropertyDTOs, "没有配置模型对应的属性");
		
		List<DataPropertyAccessDTO> dataPropertyAccessDTOs = new ArrayList<DataPropertyAccessDTO>();
		Map<Long, DataPropertyAccessDTO> propertyAccessMap = new HashMap<Long, DataPropertyAccessDTO>();
		for(DataPropertyDTO dataPropertyDTO : dataPropertyDTOs){
			DataPropertyAccessDTO dataPropertyAccessDTO = new DataPropertyAccessDTO();
			dataPropertyAccessDTO.setAccessType(dataPropertyDTO.getDefaultAccessType());
			dataPropertyAccessDTO.setPropertyCode(dataPropertyDTO.getCode());
			dataPropertyAccessDTO.setPropertyName(dataPropertyDTO.getName());
			dataPropertyAccessDTO.setPropertyDescription(dataPropertyDTO.getDescription());
			dataPropertyAccessDTOs.add(dataPropertyAccessDTO);
			propertyAccessMap.put(dataPropertyDTO.getId(), dataPropertyAccessDTO);
		}
		
		DataPropertyAccessParam dataPropertyAccessParam = new DataPropertyAccessParam();
		BeanUtils.copyProperties(param, dataPropertyAccessParam);
		dataPropertyAccessParam.setModelId(modelDTO.getId());
		List<DataPropertyAccessDO> dataPropertyAccessDOs = dataPropertyAccessDAO
				.listDataPropertyAccess(dataPropertyAccessParam);
		//如果访问控制为空，直接返回模型属性关系里的默认访问控制
		if(CollectionUtil.isEmpty(dataPropertyAccessDOs)){
			return dataPropertyAccessDTOs;
		}
		
		//覆盖默认访问控制
		for (DataPropertyAccessDO dataPropertyAccessDO : dataPropertyAccessDOs) {
			DataPropertyAccessDTO dataPropertyControlDTO = propertyAccessMap.get(dataPropertyAccessDO.getPropertyId());
			if(dataPropertyControlDTO == null){
				continue;
			}
			dataPropertyControlDTO.setAccessType(dataPropertyAccessDO.getAccessType());
		}
		return dataPropertyAccessDTOs;
	}
}
