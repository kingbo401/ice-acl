package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.DataGrantRecordDAO;
import com.kingbo401.acl.dao.DataModelPropertyRefDAO;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataModelPropertyRefManager;
import com.kingbo401.acl.manager.DataPropertyManager;
import com.kingbo401.acl.model.dto.DataModelDTO;
import com.kingbo401.acl.model.dto.DataModelPropertyRefDTO;
import com.kingbo401.acl.model.dto.DataPropertyDTO;
import com.kingbo401.acl.model.dto.param.DataModelPropertyInfoRefParam;
import com.kingbo401.acl.model.dto.param.DataModelPropertyRefParam;
import com.kingbo401.acl.model.entity.DataGrantRecordDO;
import com.kingbo401.acl.model.entity.DataModelDO;
import com.kingbo401.acl.model.entity.DataModelPropertyRefDO;
import com.kingbo401.acl.model.entity.DataPropertyDO;
import com.kingbo401.acl.utils.BizUtils;
import com.kingbo401.commons.util.CollectionUtil;

import kingbo401.iceacl.common.constant.AclConstant;
import kingbo401.iceacl.common.model.PropertyInfo;

@Service
public class DataModelPropertyRefManagerImpl implements DataModelPropertyRefManager{
	@Autowired
	private DataModelPropertyRefDAO modelPropertyRefDAO;
	@Autowired
	private DataGrantRecordDAO dataGrantRecordDAO;
	@Autowired
	private DataModelManager dataModelManager;
	@Autowired
	private DataPropertyManager dataPropertyManager;

	@Override
	public boolean addDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		Assert.notNull(dataModelPropertyRefParam, "参数不能为空");
		String appKey = dataModelPropertyRefParam.getAppKey();
		String modelCode = dataModelPropertyRefParam.getModelCode();
		Assert.hasText(appKey, "appKey 不能为null");
		Assert.hasText(modelCode, "modelCode 不能为null");
		List<String> propertyCodes = dataModelPropertyRefParam.getPropertyCodes();
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		// 根据modelCode查询模型
		DataModelDTO model = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(model, "模型不存在");
		List<DataPropertyDTO> propertyDTOs = dataPropertyManager.getDataProperties(appKey, propertyCodes);
		Map<String, DataPropertyDTO> propertyMap = propertyDTOs.stream().collect(Collectors.toMap(DataPropertyDTO::getCode, a -> a, (k1, k2) -> k1));
		Date now = new Date();
		List<DataModelPropertyRefDO> refPOs = new ArrayList<DataModelPropertyRefDO>();
		for (String propertyCode : propertyCodes) {
			// 根据propertyCode查询
			DataPropertyDTO dataProperty = propertyMap.get(propertyCode);
			Assert.notNull(dataProperty, "属性:" + propertyCode + " 不存在");
			DataModelPropertyRefDO dataModelPropertyRefDO = new DataModelPropertyRefDO();
			dataModelPropertyRefDO.setCreateTime(now);
			dataModelPropertyRefDO.setUpdateTime(now);
			dataModelPropertyRefDO.setModelId(model.getId());
			dataModelPropertyRefDO.setPropertyId(dataProperty.getId());
			dataModelPropertyRefDO.setStatus(AclConstant.STATUS_NORMAL);
			dataModelPropertyRefDO.setDefaultAccessType(AclConstant.DATA_PROPERTY_ACCESS_ALLOW);
			refPOs.add(dataModelPropertyRefDO);
		}
		modelPropertyRefDAO.batchCreate(refPOs);
		return true;
	}

	@Override
	public boolean addDataModelPropertyInfoRef(DataModelPropertyInfoRefParam dataModelPropertyInfoRefParam) {
		Assert.notNull(dataModelPropertyInfoRefParam, "参数不能为空");
		String appKey = dataModelPropertyInfoRefParam.getAppKey();
		String modelCode = dataModelPropertyInfoRefParam.getModelCode();
		Assert.hasText(appKey, "appKey 不能为null");
		Assert.hasText(modelCode, "modelCode 不能为null");
		// 根据modelCode查询模型
		DataModelDTO model = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(model, "模型不存在");
		//先删除所有关联
		List<PropertyInfo> propertyInfos = dataModelPropertyInfoRefParam.getPropertyInfos();
		Assert.notEmpty(propertyInfos, "propertyInfos不能为空");
		List<String> propertyCodes = new ArrayList<String>();
		for(PropertyInfo propertyInfo : propertyInfos){
			String code = propertyInfo.getPropertyCode();
			Assert.hasText(code, "propertyCode不能为空");
			Integer defaultAccessControl = propertyInfo.getDefaultAccessControl();
			Assert.notNull(defaultAccessControl, "defaultAccessControl不能为空");
			propertyCodes.add(code);
		}
		List<DataPropertyDTO> propertyDTOs = dataPropertyManager.getDataProperties(appKey, propertyCodes);
		Map<Object, DataPropertyDTO> propertyMap = propertyDTOs.stream().collect(Collectors.toMap(DataPropertyDTO::getCode, a -> a, (k1, k2) -> k1));
		Date now = new Date();
		List<DataModelPropertyRefDO> refPOs = new ArrayList<DataModelPropertyRefDO>();
		for (PropertyInfo propertyInfo : propertyInfos) {
			String propertyCode = propertyInfo.getPropertyCode();
			DataPropertyDTO dataProperty = propertyMap.get(propertyCode);
			Assert.notNull(dataProperty, "属性:" + propertyCode + " 不存在");
			DataModelPropertyRefDO dataModelPropertyRefDO = new DataModelPropertyRefDO();
			dataModelPropertyRefDO.setCreateTime(now);
			dataModelPropertyRefDO.setUpdateTime(now);
			dataModelPropertyRefDO.setModelId(model.getId());
			dataModelPropertyRefDO.setPropertyId(dataProperty.getId());
			dataModelPropertyRefDO.setStatus(AclConstant.STATUS_NORMAL);
			dataModelPropertyRefDO.setDefaultAccessType(propertyInfo.getDefaultAccessControl());
			refPOs.add(dataModelPropertyRefDO);
		}
		modelPropertyRefDAO.batchCreate(refPOs);
		return true;
	}

	private boolean updateDataModelPropertyRefStatus(DataModelPropertyRefParam dataModelPropertyRefParam, int status){
		String appKey = dataModelPropertyRefParam.getAppKey();
		String modelCode = dataModelPropertyRefParam.getModelCode();
		Assert.hasText(appKey, "appKey 不能为null");
		Assert.hasText(modelCode, "modelCode 不能为null");
		List<String> propertyCodes = dataModelPropertyRefParam.getPropertyCodes();
		Assert.notEmpty(propertyCodes, "propertyCodes不能为空");
		// 根据modelCode查询模型
		DataModelDTO model = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(model, "模型不存在");
		List<Long> propertyIds = new ArrayList<Long>();
		List<DataPropertyDTO> propertyDTOs = dataPropertyManager.getDataProperties(appKey, propertyCodes);
		for(DataPropertyDTO dataPropertyDTO : propertyDTOs){
			DataGrantRecordDO dataGrantRecordDO = dataGrantRecordDAO.getOneDataGrantRecord(model.getId(), null, dataPropertyDTO.getId());
			Assert.isNull(dataGrantRecordDO, "此模型-属性下有已授权的数据，不能删除；请先回收掉相关数据权限");
			propertyIds.add(dataPropertyDTO.getId());
		}
		modelPropertyRefDAO.updateRefsStatus(model.getId(), propertyIds, status);
		return true;
	}
	
	@Override
	public boolean freezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, AclConstant.STATUS_NORMAL);
	}

	@Override
	public boolean removeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, AclConstant.STATUS_REMOVE);
	}

	@Override
	public List<DataModelPropertyRefDTO> listDataPropertyRef(long modelId) {
		List<DataModelPropertyRefDO> refPOs = modelPropertyRefDAO.listRefsByModelId(modelId);
		if(CollectionUtil.isEmpty(refPOs)){
			return null;
		}
		
		List<Long> propertyIds = new ArrayList<Long>();
		Map<Long, DataModelPropertyRefDO> refMap = new HashMap<Long, DataModelPropertyRefDO>();
		for(DataModelPropertyRefDO refDO : refPOs){
			propertyIds.add(refDO.getPropertyId());
			refMap.put(refDO.getPropertyId(), refDO);
		}
		
		List<DataPropertyDTO> dataPropertyDTOs = dataPropertyManager.getDataProperties(propertyIds);
		Assert.notEmpty(dataPropertyDTOs, "获取到属性列表为空");
		List<DataModelPropertyRefDTO> refDTOs = new ArrayList<DataModelPropertyRefDTO>();
		for(DataPropertyDTO dataPropertyDTO : dataPropertyDTOs){
			DataModelPropertyRefDO refDO = refMap.get(dataPropertyDTO.getId());
			DataModelPropertyRefDTO refDTO = new DataModelPropertyRefDTO();
			refDTO.setPropertyId(dataPropertyDTO.getId());
			refDTO.setPropertyCode(dataPropertyDTO.getCode());
			refDTO.setPropertyDescription(dataPropertyDTO.getDescription());
			refDTO.setPropertyName(dataPropertyDTO.getName());
			refDTO.setDefaultAccessType(refDO.getDefaultAccessType());
			refDTOs.add(refDTO);
		}
		return refDTOs;
	}

	@Override
	public List<DataModelPropertyRefDTO> listDataPropertyRef(String appKey, String modelCode) {
		DataModelDTO model = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(model, "模型不存在");
		return listDataPropertyRef(model.getId());
	}

	@Override
	public List<DataPropertyDTO> listDataProperty(String appKey, String modelCode) {
		DataModelDTO model = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(model, "模型不存在");
		return listDataProperty(model.getId());
	}

	@Override
	public List<DataPropertyDTO> listDataProperty(long modelId) {
		List<DataPropertyDO> dataPropertyPOs = modelPropertyRefDAO.listDataPropertyByModelId(modelId);
		return BizUtils.buildDataPropertyDTOs(dataPropertyPOs);
	}

	@Override
	public List<DataModelDTO> listDataModel(long propertyId) {
		List<DataModelDO> dataModels = modelPropertyRefDAO.listDataModelByPropertyId(propertyId);
		if (CollectionUtil.isEmpty(dataModels)) {
			return null;
		}
		List<DataModelDTO> dataModelDTOs = new ArrayList<DataModelDTO>();
		for(DataModelDO dataModelDO : dataModels){
			DataModelDTO dataModelDTO = new DataModelDTO();
			BeanUtils.copyProperties(dataModelDO, dataModelDTO);
			dataModelDTOs.add(dataModelDTO);
		}
		return dataModelDTOs;
	}
}
