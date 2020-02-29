package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.dao.DataOperationDAO;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataOperationManager;
import com.kingbo401.acl.model.dto.DataModelDTO;
import com.kingbo401.acl.model.dto.DataOperationDTO;
import com.kingbo401.acl.model.entity.DataOperationDO;
import com.kingbo401.commons.util.CollectionUtil;

@Service
public class DataOperationManagerImpl implements DataOperationManager {
	@Autowired
	private DataOperationDAO dataOperationDAO;
	@Autowired
	private DataModelManager dataModelManager;

	@Override
	public DataOperationDTO create(DataOperationDTO dataOperationDTO) {
		Assert.notNull(dataOperationDTO, "参数不能为空");
		String appKey = dataOperationDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String operationCode = dataOperationDTO.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationDTO.getName(), "name不能为空");
		Long modelId = dataOperationDTO.getModelId();
		Assert.notNull(modelId, "modelId不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getById(modelId);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		Assert.isTrue(appKey.equals(dataModelDTO.getAppKey()), "不能操作其它应用的数据");
		dataOperationDTO.setStatus(AclConstant.STATUS_NORMAL);
		DataOperationDO dataOperationDO = dataOperationDAO.getByCode0(dataModelDTO.getId(), operationCode);
		if (dataOperationDO != null) {
			dataOperationDTO.setId(dataOperationDO.getId());
			BeanUtils.copyProperties(dataOperationDTO, dataOperationDO);
			dataOperationDAO.update(dataOperationDO);
		} else {
			dataOperationDO = new DataOperationDO();
			BeanUtils.copyProperties(dataOperationDTO, dataOperationDO);
			dataOperationDAO.create(dataOperationDO);
		}
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public DataOperationDTO update(DataOperationDTO dataOperationDTO) {
		Assert.notNull(dataOperationDTO, "参数不能为空");
		String appKey = dataOperationDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String operationCode = dataOperationDTO.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationDTO.getName(), "name不能为空");
		Long modelId = dataOperationDTO.getModelId();
		Assert.notNull(modelId, "modelId不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getById(modelId);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		Assert.isTrue(appKey.equals(dataModelDTO.getAppKey()), "不能操作其它应用的数据");
		DataOperationDO dataOperationDO = dataOperationDAO.getByCode(modelId, operationCode);
		Assert.notNull(dataOperationDO, "属性不存在");
		dataOperationDTO.setId(dataOperationDO.getId());
		BeanUtils.copyProperties(dataOperationDTO, dataOperationDO);
		dataOperationDAO.update(dataOperationDO);
		return buildDataOperationDTO(dataOperationDO);
	}
	
	private boolean updateDataOperationStatus(DataOperationDTO dataOperationDTO, int status){
		String appKey = dataOperationDTO.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(appKey, "appKey不能为空");
		String operationCode = dataOperationDTO.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationDTO.getName(), "name不能为空");
		Long modelId = dataOperationDTO.getModelId();
		Assert.notNull(modelId, "modelId不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getById(modelId);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		Assert.isTrue(appKey.equals(dataModelDTO.getAppKey()), "不能操作其它应用的数据");
		DataOperationDO dataOperationDO = dataOperationDAO.getByCode(modelId, operationCode);
		Assert.notNull(dataOperationDO, "属性不存在");
		dataOperationDO.setStatus(status);
		dataOperationDAO.update(dataOperationDO);
		return true;
	}

	@Override
	public boolean remove(DataOperationDTO dataOperationDTO) {
		return updateDataOperationStatus(dataOperationDTO, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freeze(DataOperationDTO dataOperationDTO) {
		return updateDataOperationStatus(dataOperationDTO, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreeze(DataOperationDTO dataOperationDTO) {
		return updateDataOperationStatus(dataOperationDTO, AclConstant.STATUS_NORMAL);

	}

	@Override
	public DataOperationDTO getByCode(String appKey, String modelCode, String operationCode) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getByCode(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationDO dataOperationDO = dataOperationDAO.getByCode(dataModelDTO.getId(), operationCode);
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public DataOperationDTO getByCode(Long modelId, String operationCode) {
		Assert.notNull(modelId, "modelId不能为空");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationDO dataOperationDO = dataOperationDAO.getByCode(modelId, operationCode);
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public List<DataOperationDTO> listDataOperation(String appKey, String modelCode) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getByCode(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		return buildDataOperationDTOs(dataOperationDAO.listByModelId(dataModelDTO.getId()));
	}

	@Override
	public List<DataOperationDTO> listDataOperation(Long modelId) {
		Assert.notNull(modelId, "modelId不能为空");
		return buildDataOperationDTOs(dataOperationDAO.listByModelId(modelId));
	}

	private DataOperationDTO buildDataOperationDTO(DataOperationDO dataOperationDO){
		if(dataOperationDO == null){
			return null;
		}
		DataOperationDTO dataOperationDTO = new DataOperationDTO();
		BeanUtils.copyProperties(dataOperationDO, dataOperationDTO);
		return dataOperationDTO;
	}
	
	private List<DataOperationDTO> buildDataOperationDTOs(List<DataOperationDO> dataOperationDOs){
		if(CollectionUtil.isEmpty(dataOperationDOs)){
			return null;
		}
		List<DataOperationDTO> dataOperationDTOs = new ArrayList<DataOperationDTO>();
		for(DataOperationDO dataOperationDO : dataOperationDOs){
			dataOperationDTOs.add(buildDataOperationDTO(dataOperationDO));
		}
		return dataOperationDTOs;
	}
}
