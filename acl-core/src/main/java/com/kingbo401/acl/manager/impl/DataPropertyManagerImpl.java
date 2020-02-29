package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.enums.DataType;
import com.kingbo401.acl.dao.DataPropertyDAO;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataPropertyManager;
import com.kingbo401.acl.model.dto.DataModelDTO;
import com.kingbo401.acl.model.dto.DataPropertyDTO;
import com.kingbo401.acl.model.entity.DataPropertyDO;

@Service
public class DataPropertyManagerImpl implements DataPropertyManager {
	@Autowired
	private DataPropertyDAO propertyDAO;
	@Autowired
	private DataModelManager dataModelManager;

	@Override
	public DataPropertyDTO create(DataPropertyDTO dataPropertyDTO) {
		String appKey = dataPropertyDTO.getAppKey();
		Long modelId = dataPropertyDTO.getModelId();
		String propertyCode = dataPropertyDTO.getCode();
		String name = dataPropertyDTO.getName();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.notNull(modelId, "modelId不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getById(modelId);
		Assert.notNull(dataModelDTO, "model不存在");
		Assert.isTrue(appKey.equals(dataModelDTO.getAppKey()), "modelId非法");
		Assert.hasText(propertyCode, "code不能为空");
		Assert.hasText(name, "name不能为空");
		String dataType = dataPropertyDTO.getDataType();
		if (dataType == null) {
			dataType = DataType.STRING.getCode();
			dataPropertyDTO.setDataType(dataType);
		}
		Assert.isTrue(DataType.isValid(dataType), "数据类型非法");
		dataPropertyDTO.setStatus(AclConstant.STATUS_NORMAL);
		DataPropertyDO dataPropertyDO = propertyDAO.getByCode0(modelId, propertyCode);
		if (dataPropertyDO != null) {
			dataPropertyDTO.setId(dataPropertyDO.getId());
			BeanUtils.copyProperties(dataPropertyDTO, dataPropertyDO);
			propertyDAO.update(dataPropertyDO);
		} else {
			dataPropertyDO = new DataPropertyDO();
			BeanUtils.copyProperties(dataPropertyDTO, dataPropertyDO);
			propertyDAO.create(dataPropertyDO);
			dataPropertyDTO.setId(dataPropertyDO.getId());
		}
		return dataPropertyDTO;
	}

	@Override
	public DataPropertyDTO update(DataPropertyDTO dataPropertyDTO) {
		String appKey = dataPropertyDTO.getAppKey();
		Long modelId = dataPropertyDTO.getModelId();
		String propertyCode = dataPropertyDTO.getCode();
		String name = dataPropertyDTO.getName();
		Assert.hasText(propertyCode, "code 不能为空");
		Assert.hasText(name, "name 不能为空");
		Assert.notNull(modelId, "modelId不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getById(modelId);
		Assert.notNull(dataModelDTO, "model不存在");
		Assert.isTrue(appKey.equals(dataModelDTO.getAppKey()), "modelId非法");
		DataPropertyDO dataPropertyDO = propertyDAO.getByCode(modelId, propertyCode);
		Assert.notNull(dataPropertyDO, "属性不存在");
		dataPropertyDTO.setId(dataPropertyDO.getId());
		BeanUtils.copyProperties(dataPropertyDTO, dataPropertyDO);
		propertyDAO.update(dataPropertyDO);
		return dataPropertyDTO;
	}

	private boolean updatePropertyStatus(DataPropertyDTO dataPropertyDTO, int status) {
		Long modelId = dataPropertyDTO.getModelId();
		String propertyCode = dataPropertyDTO.getCode();
		Assert.notNull(modelId, "modelId不能为空");
		Assert.hasText(propertyCode, "propertyCode不能为空");
		DataPropertyDO dataPropertyDO = propertyDAO.getByCode(modelId, propertyCode);
		Assert.notNull(dataPropertyDO, "属性不存在");
		dataPropertyDO.setStatus(status);
		propertyDAO.update(dataPropertyDO);
		return true;
	}

	@Override
	public boolean remove(DataPropertyDTO dataPropertyDTO) {
		return updatePropertyStatus(dataPropertyDTO, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freeze(DataPropertyDTO dataPropertyDTO) {
		return updatePropertyStatus(dataPropertyDTO, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreeze(DataPropertyDTO dataPropertyDTO) {
		return updatePropertyStatus(dataPropertyDTO, AclConstant.STATUS_NORMAL);
	}

	@Override
	public List<DataPropertyDTO> getByCodes(Long modelId, List<String> propertyCodes) {
		Assert.notNull(modelId, "modelId不能为空");
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		List<DataPropertyDO> dataPropertyDOs = propertyDAO.getByCodes(modelId, propertyCodes);
		Assert.notEmpty(dataPropertyDOs, "属性不存在");
		Map<String, DataPropertyDO> propertyMap = dataPropertyDOs.stream().collect(Collectors.toMap(DataPropertyDO::getCode, a -> a, (k1, k2) -> k1));
		for (String propertyCode : propertyCodes) {
			Assert.notNull(propertyMap.get(propertyCode), "属性:" + propertyCode + " 不存在");
		}
		return buildDataPropertyDTOs(dataPropertyDOs);
	}

	@Override
	public DataPropertyDTO getByCode(Long modelId, String propertyCode) {
		Assert.notNull(modelId, "modelId不能为空");
		Assert.hasText(propertyCode, "propertyCode不能为空");
		DataPropertyDO dataPropertyDO = propertyDAO.getByCode(modelId, propertyCode);
		return buildDataPropertyDTO(dataPropertyDO);
	}

	@Override
	public List<DataPropertyDTO> listDataProperty(Long modelId) {
		Assert.notNull(modelId, "modelId不能为空");
		List<DataPropertyDO> dataPropertyDOs = propertyDAO.listDataProperty(modelId);
		return buildDataPropertyDTOs(dataPropertyDOs);
	}
	

	private DataPropertyDTO buildDataPropertyDTO(DataPropertyDO dataPropertyDO){
		if(dataPropertyDO == null){
			return null;
		}
		DataPropertyDTO dataPropertyDTO = new DataPropertyDTO();
		BeanUtils.copyProperties(dataPropertyDO, dataPropertyDTO);
		return dataPropertyDTO;
	}

	private List<DataPropertyDTO> buildDataPropertyDTOs(List<DataPropertyDO> dataPropertyDOs) {
		List<DataPropertyDTO> lists = new ArrayList<DataPropertyDTO>();
		for (DataPropertyDO propertyDO : dataPropertyDOs) {
			lists.add(buildDataPropertyDTO(propertyDO));
		}
		return lists;
	}
}
