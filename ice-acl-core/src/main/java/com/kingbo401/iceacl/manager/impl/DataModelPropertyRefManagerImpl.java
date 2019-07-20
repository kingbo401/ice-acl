package com.kingbo401.iceacl.manager.impl;

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

import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.DataGrantRecordDAO;
import com.kingbo401.iceacl.dao.DataModelPropertyRefDAO;
import com.kingbo401.iceacl.manager.DataModelManager;
import com.kingbo401.iceacl.manager.DataModelPropertyRefManager;
import com.kingbo401.iceacl.manager.DataPropertyManager;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataModelPropertyRefDTO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.dto.param.DataModelPropertyInfoRefParam;
import com.kingbo401.iceacl.model.dto.param.DataModelPropertyRefParam;
import com.kingbo401.iceacl.model.po.DataGrantRecordPO;
import com.kingbo401.iceacl.model.po.DataModelPO;
import com.kingbo401.iceacl.model.po.DataModelPropertyRefPO;
import com.kingbo401.iceacl.model.po.DataPropertyPO;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;
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
		List<DataModelPropertyRefPO> refPOs = new ArrayList<DataModelPropertyRefPO>();
		for (String propertyCode : propertyCodes) {
			// 根据propertyCode查询
			DataPropertyDTO dataProperty = propertyMap.get(propertyCode);
			Assert.notNull(dataProperty, "属性:" + propertyCode + " 不存在");
			DataModelPropertyRefPO dataModelPropertyRefPO = new DataModelPropertyRefPO();
			dataModelPropertyRefPO.setCreateTime(now);
			dataModelPropertyRefPO.setUpdateTime(now);
			dataModelPropertyRefPO.setModelId(model.getId());
			dataModelPropertyRefPO.setPropertyId(dataProperty.getId());
			dataModelPropertyRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			dataModelPropertyRefPO.setDefaultAccessType(IceAclConstant.DATA_PROPERTY_ACCESS_ALLOW);
			refPOs.add(dataModelPropertyRefPO);
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
		List<DataModelPropertyRefPO> refPOs = new ArrayList<DataModelPropertyRefPO>();
		for (PropertyInfo propertyInfo : propertyInfos) {
			String propertyCode = propertyInfo.getPropertyCode();
			DataPropertyDTO dataProperty = propertyMap.get(propertyCode);
			Assert.notNull(dataProperty, "属性:" + propertyCode + " 不存在");
			DataModelPropertyRefPO dataModelPropertyRefPO = new DataModelPropertyRefPO();
			dataModelPropertyRefPO.setCreateTime(now);
			dataModelPropertyRefPO.setUpdateTime(now);
			dataModelPropertyRefPO.setModelId(model.getId());
			dataModelPropertyRefPO.setPropertyId(dataProperty.getId());
			dataModelPropertyRefPO.setStatus(IceAclConstant.STATUS_NORMAL);
			dataModelPropertyRefPO.setDefaultAccessType(propertyInfo.getDefaultAccessControl());
			refPOs.add(dataModelPropertyRefPO);
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
			DataGrantRecordPO dataGrantRecordPO = dataGrantRecordDAO.getOneDataGrantRecord(model.getId(), null, dataPropertyDTO.getId());
			Assert.isNull(dataGrantRecordPO, "此模型-属性下有已授权的数据，不能删除；请先回收掉相关数据权限");
			propertyIds.add(dataPropertyDTO.getId());
		}
		modelPropertyRefDAO.updateRefsStatus(model.getId(), propertyIds, status);
		return true;
	}
	
	@Override
	public boolean freezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public boolean removeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam) {
		return updateDataModelPropertyRefStatus(dataModelPropertyRefParam, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public List<DataModelPropertyRefDTO> listDataPropertyRef(long modelId) {
		List<DataModelPropertyRefPO> refPOs = modelPropertyRefDAO.listRefsByModelId(modelId);
		if(CollectionUtil.isEmpty(refPOs)){
			return null;
		}
		
		List<Long> propertyIds = new ArrayList<Long>();
		Map<Long, DataModelPropertyRefPO> refMap = new HashMap<Long, DataModelPropertyRefPO>();
		for(DataModelPropertyRefPO refPO : refPOs){
			propertyIds.add(refPO.getPropertyId());
			refMap.put(refPO.getPropertyId(), refPO);
		}
		
		List<DataPropertyDTO> dataPropertyDTOs = dataPropertyManager.getDataProperties(propertyIds);
		Assert.notEmpty(dataPropertyDTOs, "获取到属性列表为空");
		List<DataModelPropertyRefDTO> refDTOs = new ArrayList<DataModelPropertyRefDTO>();
		for(DataPropertyDTO dataPropertyDTO : dataPropertyDTOs){
			DataModelPropertyRefPO refPO = refMap.get(dataPropertyDTO.getId());
			DataModelPropertyRefDTO refDTO = new DataModelPropertyRefDTO();
			refDTO.setPropertyId(dataPropertyDTO.getId());
			refDTO.setPropertyCode(dataPropertyDTO.getCode());
			refDTO.setPropertyDescription(dataPropertyDTO.getDescription());
			refDTO.setPropertyName(dataPropertyDTO.getName());
			refDTO.setDefaultAccessType(refPO.getDefaultAccessType());
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
		List<DataPropertyPO> dataPropertyPOs = modelPropertyRefDAO.listDataPropertyByModelId(modelId);
		return BizUtils.buildDataPropertyDTOs(dataPropertyPOs);
	}

	@Override
	public List<DataModelDTO> listDataModel(long propertyId) {
		List<DataModelPO> dataModels = modelPropertyRefDAO.listDataModelByPropertyId(propertyId);
		if (CollectionUtil.isEmpty(dataModels)) {
			return null;
		}
		List<DataModelDTO> dataModelDTOs = new ArrayList<DataModelDTO>();
		for(DataModelPO dataModelPO : dataModels){
			DataModelDTO dataModelDTO = new DataModelDTO();
			BeanUtils.copyProperties(dataModelPO, dataModelDTO);
			dataModelDTOs.add(dataModelDTO);
		}
		return dataModelDTOs;
	}
}
