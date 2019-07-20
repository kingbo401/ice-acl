package com.kingbo401.iceacl.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.DataPropertyDAO;
import com.kingbo401.iceacl.manager.AppManager;
import com.kingbo401.iceacl.manager.DataModelPropertyRefManager;
import com.kingbo401.iceacl.manager.DataPropertyManager;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.po.DataPropertyPO;
import com.kingbo401.iceacl.model.po.param.DataPropertyQueryParam;
import com.kingbo401.iceacl.utils.BizUtils;

import kingbo401.iceacl.common.constant.IceAclConstant;

@Service
public class DataPropertyManagerImpl implements DataPropertyManager {
	@Autowired
	private DataPropertyDAO propertyDAO;
	@Autowired
	private DataModelPropertyRefManager dataModelPropertyRefManager;
	@Autowired
	private AppManager sysAppManager;

	@Override
	public DataPropertyDTO createDataProperty(DataPropertyDTO dataPropertyDTO) {
		Assert.notNull(dataPropertyDTO, "dataPropertyDTO 不能为空");
		String appKey = dataPropertyDTO.getAppKey();
		String propertyCode = dataPropertyDTO.getCode();
		String name = dataPropertyDTO.getName();
		Assert.hasText(appKey, "appKey 不能为空");
		Assert.notNull(sysAppManager.getAppByKey(appKey), "app不存在");
		Assert.hasText(propertyCode, "code 不能为空");
		Assert.hasText(name, "name 不能为空");
		Date now = new Date();
		dataPropertyDTO.setCreateTime(now);
		dataPropertyDTO.setUpdateTime(now);
		DataPropertyPO dataPropertyPO = propertyDAO.getByCode(appKey, propertyCode);
		Assert.isNull(dataPropertyPO, "属性已存在");
		dataPropertyPO = new DataPropertyPO();
		BeanUtils.copyProperties(dataPropertyDTO, dataPropertyPO);
		dataPropertyDTO.setId(dataPropertyPO.getId());
		return dataPropertyDTO;
	}

	@Override
	public DataPropertyDTO updateDataProperty(DataPropertyDTO dataPropertyDTO) {
		Assert.notNull(dataPropertyDTO, "dataPropertyDTO 不能为空");
		String appKey = dataPropertyDTO.getAppKey();
		String propertyCode = dataPropertyDTO.getCode();
		String name = dataPropertyDTO.getName();
		Assert.hasText(appKey, "appKey 不能为空");
		Assert.notNull(sysAppManager.getAppByKey(appKey), "app不存在");
		Assert.hasText(propertyCode, "code 不能为空");
		Assert.hasText(name, "name 不能为空");
		Date now = new Date();
		dataPropertyDTO.setUpdateTime(now);
		DataPropertyPO dataPropertyPO = propertyDAO.getByCode(appKey, propertyCode);
		Assert.notNull(dataPropertyPO, "属性不存在");
		dataPropertyPO = new DataPropertyPO();
		BeanUtils.copyProperties(dataPropertyDTO, dataPropertyPO);
		propertyDAO.update(dataPropertyPO);
		dataPropertyDTO.setId(dataPropertyPO.getId());
		return dataPropertyDTO;
	}

	private boolean updatePropertyStatus(String appKey, String propertyCode, int status) {
		Assert.hasText(appKey, "appKey不能为空");
		Assert.hasText(propertyCode, "propertyCode不能为空");
		DataPropertyPO dataPropertyPO = propertyDAO.getByCode(appKey, propertyCode);
		Assert.notNull(dataPropertyPO, "属性不存在");
		if(status == IceAclConstant.STATUS_REMOVE){
			List<DataModelDTO> dataModelDTOs = dataModelPropertyRefManager.listDataModel(dataPropertyPO.getId());
			Assert.isTrue(CollectionUtil.isEmpty(dataModelDTOs), "属性被模型使用，不能删除");
		}
		dataPropertyPO.setStatus(status);
		dataPropertyPO.setUpdateTime(new Date());
		propertyDAO.update(dataPropertyPO);
		return true;
	}

	@Override
	public boolean removeDataProperty(String appKey, String propertyCode) {
		return updatePropertyStatus(appKey, propertyCode, IceAclConstant.STATUS_REMOVE);
	}

	@Override
	public boolean freezeDataProperty(String appKey, String propertyCode) {
		return updatePropertyStatus(appKey, propertyCode, IceAclConstant.STATUS_FREEZE);
	}

	@Override
	public boolean unfreezeDataProperty(String appKey, String propertyCode) {
		return updatePropertyStatus(appKey, propertyCode, IceAclConstant.STATUS_NORMAL);
	}

	@Override
	public List<DataPropertyDTO> getDataProperties(List<Long> ids) {
		Assert.notEmpty(ids, "ids不能为空");
		List<DataPropertyPO> dataPropertyPOs = propertyDAO.getByIds(ids);
		Assert.notEmpty(dataPropertyPOs, "属性不存在");
		Map<Long, DataPropertyPO> propertyMap = dataPropertyPOs.stream().collect(Collectors.toMap(DataPropertyPO::getId, a -> a, (k1, k2) -> k1));
		for (Long id : ids) {
			Assert.notNull(propertyMap.get(id), "属性:" + id + " 不存在");
		}
		return BizUtils.buildDataPropertyDTOs(dataPropertyPOs);
	}

	@Override
	public List<DataPropertyDTO> getDataProperties(String appKey, List<String> propertyCodes) {
		Assert.hasText(appKey, "appKey 不能为空");
		Assert.notEmpty(propertyCodes, "propertyCodes 不能为空");
		List<DataPropertyPO> dataPropertyPOs = propertyDAO.getByCodes(appKey, propertyCodes);
		Assert.notEmpty(dataPropertyPOs, "属性不存在");
		Map<String, DataPropertyPO> propertyMap = dataPropertyPOs.stream().collect(Collectors.toMap(DataPropertyPO::getCode, a -> a, (k1, k2) -> k1));
		for (String propertyCode : propertyCodes) {
			Assert.notNull(propertyMap.get(propertyCode), "属性:" + propertyCode + " 不存在");
		}
		return BizUtils.buildDataPropertyDTOs(dataPropertyPOs);
	}

	@Override
	public DataPropertyDTO getDataProperty(String appKey, String propertyCode) {
		DataPropertyPO dataPropertyPO = propertyDAO.getByCode(appKey, propertyCode);
		return BizUtils.buildDataPropertyDTO(dataPropertyPO);
	}

	@Override
	public List<DataPropertyDTO> listDataProperty(DataPropertyQueryParam param) {
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		List<DataPropertyPO> dataPropertyPOs = propertyDAO.listDataProperty(param);
		return BizUtils.buildDataPropertyDTOs(dataPropertyPOs);
	}

	@Override
	public PageVO<DataPropertyDTO> pageDataProperty(DataPropertyQueryParam param) {
		Assert.hasText(param.getAppKey(), "appKey不能为空");
		PageVO<DataPropertyDTO> pageVO = new PageVO<DataPropertyDTO>(param);
		if(param.isReturnTotalCount()){
			long total = propertyDAO.countDataProperty(param);
			pageVO.setTotal(total);
			if(total == 0){
				return pageVO;
			}
		}
		List<DataPropertyPO> dataPropertyPOs = propertyDAO.pageDataProperty(param);
		pageVO.setItems(BizUtils.buildDataPropertyDTOs(dataPropertyPOs));
		return pageVO;
	}
}
