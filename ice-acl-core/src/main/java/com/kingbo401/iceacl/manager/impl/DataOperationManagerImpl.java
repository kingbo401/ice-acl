package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.DataGrantRecordDAO;
import com.kingbo401.iceacl.dao.DataOperationDAO;
import com.kingbo401.iceacl.manager.DataModelManager;
import com.kingbo401.iceacl.manager.DataOperationManager;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataOperationDTO;
import com.kingbo401.iceacl.model.dto.param.DataOperationParam;
import com.kingbo401.iceacl.model.po.DataGrantRecordPO;
import com.kingbo401.iceacl.model.po.DataOperationPO;

import kingbo401.iceacl.common.constant.IceAclConstant;

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
		DataOperationPO dataOperationPO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.isNull(dataOperationPO, "属性code已被使用");
		dataOperationPO = new DataOperationPO();
		BeanUtils.copyProperties(dataOperationParam, dataOperationPO);
		Date now = new Date();
		dataOperationPO.setCreateTime(now);
		dataOperationPO.setUpdateTime(now);
		dataOperationPO.setStatus(IceAclConstant.STATUS_NORMAL);
		dataOperationDAO.create(dataOperationPO);
		return buildDataOperationDTO(dataOperationPO);
	}

	@Override
	public DataOperationDTO updateDataOperation(DataOperationParam dataOperationParam) {
		Assert.notNull(dataOperationParam, "参数不能为空");
		Assert.notNull(dataOperationParam);
		String appKey = dataOperationParam.getAppKey();
		String operationCode = dataOperationParam.getCode();
		Assert.hasText(operationCode, "operationCode不能为空");
		Assert.hasText(dataOperationParam.getName(), "name不能为空");
		String modelCode = dataOperationParam.getModelCode();
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "数据模型不存在");
		DataOperationPO dataOperationPO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationPO, "属性不存在");
		Date now = new Date();
		dataOperationPO.setUpdateTime(now);
		dataOperationPO.setDescription(dataOperationParam.getDescription());
		dataOperationPO.setName(dataOperationParam.getName());
		dataOperationDAO.update(dataOperationPO);
		return buildDataOperationDTO(dataOperationPO);
	}
	
	private boolean updateDataOperationStatus(String appKey, String modelCode, String operationCode, int status){
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationPO dataOperationPO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationPO, "操作不存在");
		if(status == IceAclConstant.STATUS_REMOVE){
			DataGrantRecordPO dataGrantRecordPO = dataGrantRecordDAO.getOneDataGrantRecord(dataOperationPO.getModelId(), dataOperationPO.getId(), null);
			Assert.isNull(dataGrantRecordPO, "此模型-操作下有已授权的数据，不能删除；请先回收掉相关数据权限");
		}
		dataOperationPO.setStatus(status);
		dataOperationPO.setUpdateTime(new Date());
		dataOperationDAO.update(dataOperationPO);
		return true;
	}

	@Override
	public boolean removeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataOperation(String appKey, String modelCode, String operationCode) {
		return updateDataOperationStatus(appKey, modelCode, operationCode, IceAclConstant.STATUS_NORMAL);

	}

	@Override
	public DataOperationDTO getDataOperation(String appKey, String modelCode, String operationCode) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(appKey, modelCode);
		Assert.notNull(dataModelDTO, "模型不存在");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationPO dataOperationPO = dataOperationDAO.getOperationByCode(dataModelDTO.getId(), operationCode);
		return buildDataOperationDTO(dataOperationPO);
	}

	@Override
	public DataOperationDTO getDataOperation(Long modelId, String operationCode) {
		Assert.notNull(modelId, "modelId不能为空");
		Assert.hasText(operationCode, "operationCode不能为空");
		DataOperationPO dataOperationPO = dataOperationDAO.getOperationByCode(modelId, operationCode);
		return buildDataOperationDTO(dataOperationPO);
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

	private DataOperationDTO buildDataOperationDTO(DataOperationPO dataOperationPO){
		if(dataOperationPO == null){
			return null;
		}
		DataOperationDTO dataOperationDTO = new DataOperationDTO();
		BeanUtils.copyProperties(dataOperationPO, dataOperationDTO);
		return dataOperationDTO;
	}
	
	private List<DataOperationDTO> buildDataOperationDTOs(List<DataOperationPO> dataOperationPOs){
		if(CollectionUtil.isEmpty(dataOperationPOs)){
			return null;
		}
		List<DataOperationDTO> dataOperationDTOs = new ArrayList<DataOperationDTO>();
		for(DataOperationPO dataOperationPO : dataOperationPOs){
			dataOperationDTOs.add(buildDataOperationDTO(dataOperationPO));
		}
		return dataOperationDTOs;
	}
}
