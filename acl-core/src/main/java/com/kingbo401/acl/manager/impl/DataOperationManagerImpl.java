package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.DataGrantRecordDAO;
import com.kingbo401.acl.dao.DataOperationDAO;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataOperationManager;
import com.kingbo401.acl.model.dto.DataModelDTO;
import com.kingbo401.acl.model.dto.DataOperationDTO;
import com.kingbo401.acl.model.dto.param.DataOperationParam;
import com.kingbo401.acl.model.entity.DataGrantRecordDO;
import com.kingbo401.acl.model.entity.DataOperationDO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class DataOperationManagerImpl implements DataOperationManager {
	@Autowired
	private DataOperationDAO dataOperationDAO;
	@Autowired
	private DataGrantRecordDAO dataGrantRecordDAO;
	@Autowired
	private DataModelManager dataModelManager;

	@Override
	public DataOperationDTO createDataOperation(DataOperationParam dataOperationParam) {
		Assert.notNull(dataOperationParam, "参数不能为空");
		String appKey = dataOperationParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		String operationCode = dataOperationParam.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationParam.getName(), "name不能为空");
		String modelCode = dataOperationParam.getModelCode();
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		DataOperationDO dataOperationDO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.isNull(dataOperationDO, "属性code已被使用");
		dataOperationDO = new DataOperationDO();
		BeanUtils.copyProperties(dataOperationParam, dataOperationDO);
		Date now = new Date();
		dataOperationDO.setCreateTime(now);
		dataOperationDO.setUpdateTime(now);
		dataOperationDO.setStatus(AclConstant.STATUS_NORMAL);
		dataOperationDAO.create(dataOperationDO);
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public DataOperationDTO updateDataOperation(DataOperationParam dataOperationParam) {
		Assert.notNull(dataOperationParam, "参数不能为空");
		String appKey = dataOperationParam.getAppKey();
		String operationCode = dataOperationParam.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationParam.getName(), "name不能为空");
		String modelCode = dataOperationParam.getModelCode();
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		DataOperationDO dataOperationDO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDO, "属性不存在");
		Date now = new Date();
		dataOperationDO.setUpdateTime(now);
		dataOperationDO.setDescription(dataOperationParam.getDescription());
		dataOperationDO.setName(dataOperationParam.getName());
		dataOperationDAO.update(dataOperationDO);
		return buildDataOperationDTO(dataOperationDO);
	}
	
	private boolean updateDataOperationStatus(String appKey, String modelCode, String operationCode, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationDO dataOperationDO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDO, "操作不存在");
		if(status == AclConstant.STATUS_REMOVE){
			DataGrantRecordDO dataGrantRecordDO = dataGrantRecordDAO.getOneDataGrantRecord(dataOperationDO.getModelId(), dataOperationDO.getId(), null);
			Assert.isNull(dataGrantRecordDO, "此模型-操作下有已授权的数据，不能删除；请先回收掉相关数据权限");
		}
		dataOperationDO.setStatus(status);
		dataOperationDO.setUpdateTime(new Date());
		dataOperationDAO.update(dataOperationDO);
		return true;
	}

	@Override
	public boolean removeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, AclConstant.STATUS_NORMAL);

	}

	@Override
	public DataOperationDTO getDataOperation(String appKey, String modelCode, String operationCode) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationDO dataOperationDO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public DataOperationDTO getDataOperation(Long modelId, String operationCode) {
		Assert.notNull(modelId, "modelId不能为空");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationDO dataOperationDO = dataOperationDAO.getOperationByCode(modelId, operationCode);
		return buildDataOperationDTO(dataOperationDO);
	}

	@Override
	public List<DataOperationDTO> listDataOperation(String appKey, String modelCode) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		return buildDataOperationDTOs(dataOperationDAO.listDataOperationByModelId(dataModelDTO.getId()));
	}

	@Override
	public List<DataOperationDTO> listDataOperation(Long modelId) {
		Assert.notNull(modelId, "modelId不能为空");
		return buildDataOperationDTOs(dataOperationDAO.listDataOperationByModelId(modelId));
	}

	private DataOperationDTO buildDataOperationDTO(DataOperationDO dataOperationDO){
		if(dataOperationDO == null){
			return null;
		}
		DataOperationDTO dataOperationDTO = new DataOperationDTO();
		BeanUtils.copyProperties(dataOperationDO, dataOperationDTO);
		return dataOperationDTO;
	}
	
	private List<DataOperationDTO> buildDataOperationDTOs(List<DataOperationDO> dataOperationPOs){
		if(CollectionUtil.isEmpty(dataOperationPOs)){
			return null;
		}
		List<DataOperationDTO> dataOperationDTOs = new ArrayList<DataOperationDTO>();
		for(DataOperationDO dataOperationDO : dataOperationPOs){
			dataOperationDTOs.add(buildDataOperationDTO(dataOperationDO));
		}
		return dataOperationDTOs;
	}
}
