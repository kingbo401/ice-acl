package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.acl.dao.DataModelDAO;
import com.kingbo401.acl.manager.AppManager;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataPropertyManager;
import com.kingbo401.acl.model.dto.DataModelDTO;
import com.kingbo401.acl.model.dto.DataPropertyDTO;
import com.kingbo401.acl.model.entity.DataModelDO;
import com.kingbo401.acl.model.entity.param.DataModelQueryParam;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.common.constant.AclConstant;

@Service
public class DataModelManagerImpl implements DataModelManager {
	private static final Pattern DATA_MODEL_NAME_PATTERN = Pattern.compile("^[a-z0-9\\._]+$");
	@Autowired
	private DataModelDAO dataModelDAO;
	@Autowired
	private AppManager appManager;
	@Autowired
	private DataPropertyManager dataPropertyManager;

	private void assertModelCode(String modelCode) {
		Assert.hasText(modelCode, "模型编码不能为空");
		Assert.isTrue(DATA_MODEL_NAME_PATTERN.matcher(modelCode).matches(), "数据模型code只能由小写字母、数字、下划线或小数点组成");
		String[] subCodes = modelCode.split("\\.");
		if (subCodes.length == 1) {
			subCodes = modelCode.split("_");
		}
		Assert.isTrue(subCodes.length >= 3, "数据模型code格式必须为kingbo.ice.user或者kingbo_ice_user,小数点或下划线不能少于两个");
	}

	public DataModelDTO createDataModel(DataModelDTO dataModelDTO) {
		Assert.notNull(dataModelDTO, "dataModelDTO 不能为空");
		String appKey = dataModelDTO.getAppKey();
		Assert.hasText(appKey, "appKey 不能为空");
		String name = dataModelDTO.getName();
		Assert.hasText(name, "name 不能为空");
		String modelCode = dataModelDTO.getCode();
		assertModelCode(modelCode);
		Assert.notNull(appManager.getAppByKey(appKey), "app不存在");

		DataModelDO dataModelDO = dataModelDAO.getModelByCode(modelCode);
		Assert.isNull(dataModelDO, "模型code已存在");
		dataModelDTO.setId(dataModelDO.getId());
		dataModelDTO.setStatus(AclConstant.STATUS_NORMAL);
		BeanUtils.copyProperties(dataModelDTO, dataModelDO);
		dataModelDAO.create(dataModelDO);
		dataModelDTO.setId(dataModelDO.getId());
		return dataModelDTO;
	}

	@Override
	public DataModelDTO updateDataModel(DataModelDTO dataModelDTO) {
		Assert.notNull(dataModelDTO, "dataModelDTO 不能为空");
		String appKey = dataModelDTO.getAppKey();
		Assert.hasText(appKey, "appKey 不能为空");
		String name = dataModelDTO.getName();
		Assert.hasText(name, "name 不能为空");
		String modelCode = dataModelDTO.getCode();
		assertModelCode(modelCode);
		DataModelDO dataModelDO = dataModelDAO.getModelByCode(modelCode);
		Assert.notNull(dataModelDO, "模型不存在");
		dataModelDTO.setId(dataModelDO.getId());
		BeanUtils.copyProperties(dataModelDTO, dataModelDO);
		dataModelDAO.update(dataModelDO);
		return dataModelDTO;
	}

	private boolean updateDataModelStatus(DataModelDTO dataModel, int status) {
		String appKey = dataModel.getAppKey();
		String modelCode = dataModel.getCode();
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelDO dataModelDO = dataModelDAO.getModelByCode(modelCode);
		Assert.notNull(dataModelDO, "模型不存在");
		Assert.isTrue(appKey.equals(dataModelDO.getAppKey()), "appkey模型不匹配");
		List<DataPropertyDTO> properties = dataPropertyManager.listDataProperty(dataModelDO.getId());
		Assert.isTrue(CollectionUtil.isEmpty(properties), "请先解除数据模型与属性的绑定关系");
		dataModelDO.setStatus(status);
		dataModelDAO.update(dataModelDO);
		return true;
	}

	@Override
	public boolean removeDataModel(DataModelDTO dataModel) {
		return updateDataModelStatus(dataModel, AclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeDataModel(DataModelDTO dataModel) {
		return updateDataModelStatus(dataModel, AclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataModel(DataModelDTO dataModel) {
		return updateDataModelStatus(dataModel, AclConstant.STATUS_NORMAL);
	}

	@Override
	public DataModelDTO getDataModel(Long id) {
		Assert.notNull(id, "id不能为空");
		DataModelDO dataModelDO = dataModelDAO.getModelById(id);
		return buildDataModelDTO(dataModelDO);
	}

	@Override
	public DataModelDTO getDataModel(String modelCode) {
		Assert.hasText(modelCode, "modelCode 不能为空");
		DataModelDO dataModelDO = dataModelDAO.getModelByCode(modelCode);
		return buildDataModelDTO(dataModelDO);
	}

	@Override
	public DataModelDTO getDataModel(String appKey, String modelCode) {
		Assert.hasText(modelCode, "modelCode 不能为空");
		DataModelDO dataModelDO = dataModelDAO.getModelByCode(modelCode);
		if (dataModelDO == null) {
			return null;
		}
		Assert.isTrue(appKey.equals(dataModelDO.getAppKey()), "appkey应用不存在");
		return buildDataModelDTO(dataModelDO);
	}

	@Override
	public List<DataModelDTO> listDataModel(DataModelQueryParam dataModelQueryParam) {
		Assert.notNull(dataModelQueryParam, "参数不能为空");
		Assert.hasText(dataModelQueryParam.getCode(), "code 不能为空");
		Assert.hasText(dataModelQueryParam.getAppKey(), "appKey 不能为空");
		List<DataModelDO> dataModelDOs = dataModelDAO.listDataModel(dataModelQueryParam);
		return buildDataModelDTOs(dataModelDOs);
	}

	@Override
	public PageVO<DataModelDTO> pageDataModel(DataModelQueryParam dataModelQueryParam) {
		Assert.notNull(dataModelQueryParam, "参数不能为空");
		PageVO<DataModelDTO> pageVO = new PageVO<DataModelDTO>(dataModelQueryParam);
		if(dataModelQueryParam.isReturnTotalCount()){
			long total = dataModelDAO.countDataModel(dataModelQueryParam);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<DataModelDO> dataModelDOs = dataModelDAO.pageDataModel(dataModelQueryParam);
		pageVO.setItems(buildDataModelDTOs(dataModelDOs));
		return pageVO;
	}

	private DataModelDTO buildDataModelDTO(DataModelDO dataModelDO){
		if(dataModelDO == null){
			return null;
		}
		DataModelDTO dataModelDTO = new DataModelDTO();
		BeanUtils.copyProperties(dataModelDO, dataModelDTO);
		return dataModelDTO;
	}
	
	private List<DataModelDTO> buildDataModelDTOs(List<DataModelDO> dataModelDOs) {
		if (CollectionUtil.isEmpty(dataModelDOs)) {
			return null;
		}
		List<DataModelDTO> list = new ArrayList<DataModelDTO>();
		for (DataModelDO dataModelDO : dataModelDOs) {
			list.add(buildDataModelDTO(dataModelDO));
		}
		return list;
	}
}
