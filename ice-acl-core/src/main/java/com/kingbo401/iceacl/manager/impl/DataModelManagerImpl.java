package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.DataModelDAO;
import com.kingbo401.iceacl.manager.AppManager;
import com.kingbo401.iceacl.manager.DataModelManager;
import com.kingbo401.iceacl.manager.DataModelPropertyRefManager;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.po.DataModelPO;
import com.kingbo401.iceacl.model.po.param.DataModelQueryParam;

import kingbo401.iceacl.common.constant.IceAclConstant;

@Service
public class DataModelManagerImpl implements DataModelManager {
	private static final Pattern DATA_MODEL_NAME_PATTERN = Pattern.compile("^[a-z0-9\\._]+$");
	@Autowired
	private DataModelDAO dataModelDAO;
	@Autowired
	private DataModelPropertyRefManager dataModelPropertyRefManager;
	@Autowired
	private AppManager appManager;

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

		DataModelPO dataModelPO = dataModelDAO.getModelByCode(modelCode);
		Assert.isNull(dataModelPO, "模型code已存在");

		dataModelDTO.setStatus(IceAclConstant.STATUS_NORMAL);
		Date now = new Date();
		dataModelDTO.setUpdateTime(now);
		dataModelDTO.setCreateTime(now);
		dataModelPO = new DataModelPO();
		BeanUtils.copyProperties(dataModelDTO, dataModelPO);
		dataModelDAO.create(dataModelPO);
		dataModelDTO.setId(dataModelPO.getId());
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
		DataModelPO dataModelPO = dataModelDAO.getModelByCode(modelCode);
		Assert.notNull(dataModelPO, "模型不存在");
		dataModelPO.setName(dataModelDTO.getName());
		dataModelPO.setDescription(dataModelDTO.getDescription());
		dataModelPO.setUpdateTime(new Date());
		dataModelDAO.update(dataModelPO);
		dataModelDTO.setId(dataModelPO.getId());
		return dataModelDTO;
	}

	private boolean updateDataModelStatus(String appKey, String modelCode, int status) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(modelCode, "modelCode不能为空");
		DataModelPO dataModelPO = dataModelDAO.getModelByCode(modelCode);
		Assert.notNull(dataModelPO, "模型不存在");
		Assert.isTrue(appKey.equals(dataModelPO.getAppKey()), "appkey模型不匹配");
		List<DataPropertyDTO> properties = dataModelPropertyRefManager.listDataProperty(dataModelPO.getId());
		Assert.isTrue(CollectionUtil.isEmpty(properties), "请先解除数据模型与属性的绑定关系");
		dataModelPO.setStatus(status);
		dataModelPO.setUpdateTime(new Date());
		dataModelDAO.update(dataModelPO);
		return true;
	}

	@Override
	public boolean removeDataModel(String appKey, String modelCode) {
		return updateDataModelStatus(appKey, modelCode, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeDataModel(String appKey, String modelCode) {
		return updateDataModelStatus(appKey, modelCode, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataModel(String appKey, String modelCode) {
		return updateDataModelStatus(appKey, modelCode, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public DataModelDTO getDataModel(Long id) {
		Assert.notNull(id, "id不能为空");
		DataModelPO dataModelPO = dataModelDAO.getModelById(id);
		return buildDataModelDTO(dataModelPO);
	}

	@Override
	public DataModelDTO getDataModel(String modelCode) {
		Assert.hasText(modelCode, "modelCode 不能为空");
		DataModelPO dataModelPO = dataModelDAO.getModelByCode(modelCode);
		return buildDataModelDTO(dataModelPO);
	}

	@Override
	public DataModelDTO getDataModel(String appKey, String modelCode) {
		Assert.hasText(modelCode, "modelCode 不能为空");
		DataModelPO dataModelPO = dataModelDAO.getModelByCode(modelCode);
		if (dataModelPO == null) {
			return null;
		}
		Assert.isTrue(appKey.equals(dataModelPO.getAppKey()), "appkey应用不存在");
		return buildDataModelDTO(dataModelPO);
	}

	@Override
	public List<DataModelDTO> listDataModel(DataModelQueryParam dataModelQueryParam) {
		Assert.notNull(dataModelQueryParam, "参数不能为空");
		Assert.hasText(dataModelQueryParam.getCode(), "code 不能为空");
		Assert.hasText(dataModelQueryParam.getAppKey(), "appKey 不能为空");
		List<DataModelPO> dataModelPOs = dataModelDAO.listDataModel(dataModelQueryParam);
		return buildDataModelDTOs(dataModelPOs);
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
		List<DataModelPO> dataModelPOs = dataModelDAO.pageDataModel(dataModelQueryParam);
		pageVO.setItems(buildDataModelDTOs(dataModelPOs));
		return pageVO;
	}

	private DataModelDTO buildDataModelDTO(DataModelPO dataModelPO){
		if(dataModelPO == null){
			return null;
		}
		DataModelDTO dataModelDTO = new DataModelDTO();
		BeanUtils.copyProperties(dataModelPO, dataModelDTO);
		return dataModelDTO;
	}
	
	private List<DataModelDTO> buildDataModelDTOs(List<DataModelPO> dataModelPOs) {
		if (CollectionUtil.isEmpty(dataModelPOs)) {
			return null;
		}
		List<DataModelDTO> list = new ArrayList<DataModelDTO>();
		for (DataModelPO dataModelPO : dataModelPOs) {
			list.add(buildDataModelDTO(dataModelPO));
		}
		return list;
	}
}
