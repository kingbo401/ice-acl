package com.kingbo401.iceacl.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kingbo401.iceacl.common.constant.IceAclConstant;
import kingbo401.iceacl.common.enums.GrantTargetType;
import kingbo401.iceacl.common.model.PropertyValue;
import kingbo401.iceacl.common.utils.MixAll;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;
import com.kingbo401.iceacl.dao.DataGrantRecordDAO;
import com.kingbo401.iceacl.dao.DataGrantRecordDetailDAO;
import com.kingbo401.iceacl.dao.RoleDAO;
import com.kingbo401.iceacl.dao.UserRoleRefDAO;
import com.kingbo401.iceacl.manager.DataAccessManager;
import com.kingbo401.iceacl.manager.DataModelManager;
import com.kingbo401.iceacl.manager.DataModelPropertyRefManager;
import com.kingbo401.iceacl.manager.DataOperationManager;
import com.kingbo401.iceacl.manager.PermissionGroupManager;
import com.kingbo401.iceacl.manager.RolePermissionGroupRefManager;
import com.kingbo401.iceacl.manager.UserPermissionGroupRefManager;
import com.kingbo401.iceacl.model.db.DataGrantRecordDO;
import com.kingbo401.iceacl.model.db.DataGrantRecordDetailDO;
import com.kingbo401.iceacl.model.db.RoleDO;
import com.kingbo401.iceacl.model.db.param.DataGrantPropertyValueParam;
import com.kingbo401.iceacl.model.db.param.DataGrantRecordParam;
import com.kingbo401.iceacl.model.db.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.iceacl.model.db.param.UserRoleRefQueryParam;
import com.kingbo401.iceacl.model.db.vo.UserRoleRefVO;
import com.kingbo401.iceacl.model.dto.DataGrantRecordDTO;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataOperationDTO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.dto.DatasCheckResult;
import com.kingbo401.iceacl.model.dto.PermissionGroupDTO;
import com.kingbo401.iceacl.model.dto.param.DataCheckParam;
import com.kingbo401.iceacl.model.dto.param.DataGrantParam;
import com.kingbo401.iceacl.model.dto.param.DataGrantQueryParam;
import com.kingbo401.iceacl.model.dto.param.DataRevokeParam;
import com.kingbo401.iceacl.model.dto.param.DatasCheckParam;

@Service
public class DataAccessManagerImpl implements DataAccessManager{
	@Autowired
	private DataGrantRecordDAO dataGrantRecordDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private UserRoleRefDAO userRoleRefDAO;
	@Autowired
	private DataGrantRecordDetailDAO dataGrantRecordDetailDAO;
	@Autowired
	private DataModelManager dataModelManager;
	@Autowired
	private DataOperationManager dataOperationManager;
	@Autowired
	private DataModelPropertyRefManager dataModelPropertyRefManager;
	@Autowired
	private RolePermissionGroupRefManager rolePermissionGroupRefManager;
	@Autowired
	private UserPermissionGroupRefManager userPermissionGroupRefManager;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
    
	public boolean grantDataPermission(DataGrantParam dataGrantParam) {
		Assert.notNull(dataGrantParam, "参数不能为空");
		String appKey = dataGrantParam.getAppKey();
		String modelCode = dataGrantParam.getModelCode();
		
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		
		String operationCode = dataGrantParam.getOperationCode();
		DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
		
		Date effectiveTime =  dataGrantParam.getEffectiveTime();
		Date expireTime =  dataGrantParam.getExpireTime();
		MixAll.checkEffectiveExpireTime(effectiveTime, expireTime);
		String grantTargetId = dataGrantParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		
		int grantTargetType = dataGrantParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = dataGrantParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		if(grantTargetType == GrantTargetType.PERMISSION_GROUP.getCode()){
			long groupId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(groupId != 0, "grantTargetId非法");
			PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
			Assert.notNull(permissionGroupDTO, "权限组不存在：" + grantTargetId);
			Assert.isTrue(permissionGroupDTO.getAppKey().equals(appKey), "此权限组不属于app:" + appKey);
		}
		if(grantTargetType == GrantTargetType.ROLE.getCode()){//如果授权对象是角色，判断角色是否为空
			RoleDO roleDO = null;
			if(grantTargetId.contains("#")){
				String[] targets = grantTargetId.split("#");
				Assert.isTrue(targets.length == 2, "角色信息格式为appkey#role");
				roleDO = roleDAO.getRoleByKey(targets[0], targets[1]);
			}else{
				long roleId = NumberUtils.toLong(grantTargetId);
				Assert.isTrue(roleId > 0, "角色id必须为整数");
				roleDO = roleDAO.getRoleById(roleId);
			}
			Assert.notNull(roleDO, "角色不存在");
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "此角色不属于app:" + appKey);
		}
		
		List<Map<String, PropertyValue>> datas = dataGrantParam.getDatas();
		Assert.notEmpty(datas, "datas不能为空");
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), modelCode);
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		Map<String, DataPropertyDTO> propertyCodeMap = Maps.newHashMap();
		Map<Long, DataPropertyDTO> propertyIdMap = Maps.newHashMap();
		for(DataPropertyDTO dto : dataProperties){
			propertyCodeMap.put(dto.getCode(), dto);
			propertyIdMap.put(dto.getId(), dto);
		}
		
		//判断dataList是否包含非法值
		for(Map<String, PropertyValue> data : datas){
			Assert.isTrue(data.size() == dataProperties.size(), "授权属性值不完整，必须与配置的属性列表一致");
			for(Map.Entry<String, PropertyValue> codeValueEntry : data.entrySet()){
				String propertyCode = codeValueEntry.getKey();
				PropertyValue propertyValue = codeValueEntry.getValue();
				DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "propertyCode非法");
				Assert.notNull(propertyValue, dataPropertyDTO.getName() + "值不能为空");
				Assert.hasText(propertyValue.getPropertyValue(), dataPropertyDTO.getName() + "值不能为空");
				Assert.hasText(propertyValue.getPropertyValueDesc(), dataPropertyDTO.getName() + "值描述不能为空");
			}
		}
		
		for(Map<String, PropertyValue> data : datas){
			Map<Long, List<String>> propertyValuesMap = Maps.newHashMap();
			for(Map.Entry<String, PropertyValue> codeValueEntry : data.entrySet()){
				String propertyCode = codeValueEntry.getKey();
				PropertyValue propertyValue = codeValueEntry.getValue();
				DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
				List<String> values = Lists.newArrayList();
				values.add(propertyValue.getPropertyValue());
				values.add(IceAclConstant.GRANTED_ALL_DATA_SYMBOL);
				propertyValuesMap.put(dataPropertyDTO.getId(), values);
			}
			DataGrantPropertyValueParam dataGrantPropertyDataParam = new DataGrantPropertyValueParam(); 
			List<String> grantTargetIds = Lists.newArrayList();
			grantTargetIds.add(grantTargetId);
			dataGrantPropertyDataParam.setGrantTargetIds(grantTargetIds);
			dataGrantPropertyDataParam.setGrantTargetType(grantTargetType);
			dataGrantPropertyDataParam.setModelId(dataModelDTO.getId());
			dataGrantPropertyDataParam.setOperationId(dataOperationDTO.getId());
			dataGrantPropertyDataParam.setTenant(tenant);
			dataGrantPropertyDataParam.setPropertyValuesMap(propertyValuesMap);
			dataGrantPropertyDataParam.setReturnNotEffective(true);
			List<Long> recordIds = dataGrantRecordDAO.listIdByPropertyValues(dataGrantPropertyDataParam);
			Date now = new Date();
			if(CollectionUtil.isNotEmpty(recordIds)){//说明存在已授权的数据，做更新
				List<DataGrantRecordDO> dataGrantRecordDOs = dataGrantRecordDAO.getByIds(recordIds);
				for(DataGrantRecordDO dataGrantRecordDO : dataGrantRecordDOs){//recordIds长度一般不会超过2个,所以这里不会特别影响效率
					boolean valueMatch = true;
					boolean descMatch = true;
					List<DataGrantRecordDetailDO> detailDOs = dataGrantRecordDetailDAO.listDetailByRecordId(dataGrantRecordDO.getId());
					for(DataGrantRecordDetailDO detailDO : detailDOs){
						DataPropertyDTO dataPropertyDTO = propertyIdMap.get(detailDO.getPropertyId());
						PropertyValue propertyValue = data.get(dataPropertyDTO.getCode());
						if(!StringUtils.equals(propertyValue.getPropertyValue(), detailDO.getPropertyValue())){
							valueMatch = false;
						}
						if(!StringUtils.equals(propertyValue.getPropertyValueDesc(), detailDO.getPropertyValueDesc())){
							descMatch = false;
						}
					}
					if(!valueMatch){//属性值没有完全匹配，不更新
						continue;
					}
					if(!descMatch){//说明属性值描述信息有更新
						//删除老数据，重新插入
						dataGrantRecordDetailDAO.removeByRecordId(dataGrantRecordDO.getId());
						List<DataGrantRecordDetailDO> newDetailDOs = buildDataGrantRecordDetailDOs(dataGrantRecordDO.getId(), data, propertyCodeMap);
						dataGrantRecordDetailDAO.batchInsert(newDetailDOs);
					}
					if(isDataGrantChange(dataGrantRecordDO, dataGrantParam)){//失效日期有变化
						dataGrantRecordDO.setEffectiveTime(dataGrantParam.getEffectiveTime());
						dataGrantRecordDO.setExpireTime(dataGrantParam.getExpireTime());
						dataGrantRecordDO.setUpdateTime(now);
						//更新dataGrantRecordDO
						dataGrantRecordDAO.update(dataGrantRecordDO);
					}
				}
				continue;
			}
			DataGrantRecordDO dataGrantRecordDO = new DataGrantRecordDO();
			dataGrantRecordDO.setModelId(dataModelDTO.getId());
			dataGrantRecordDO.setOperationId(dataOperationDTO.getId());
			dataGrantRecordDO.setStatus(IceAclConstant.STATUS_NORMAL);
			dataGrantRecordDO.setEffectiveTime(dataGrantParam.getEffectiveTime());
			dataGrantRecordDO.setExpireTime(dataGrantParam.getExpireTime());
			dataGrantRecordDO.setCreateTime(now);
			dataGrantRecordDO.setUpdateTime(now);
			dataGrantRecordDO.setGrantTargetId(grantTargetId);
			dataGrantRecordDO.setGrantTargetType(grantTargetType);
			dataGrantRecordDO.setTenant(tenant);
			dataGrantRecordDAO.create(dataGrantRecordDO);
			List<DataGrantRecordDetailDO> detailDOs = buildDataGrantRecordDetailDOs(dataGrantRecordDO.getId(), data, propertyCodeMap);
			dataGrantRecordDetailDAO.batchInsert(detailDOs);
		}
		return true;
	}
	
	private boolean isDataGrantChange(DataGrantRecordDO dataGrantRecordDO, DataGrantParam dataGrantParam){
		if(dataGrantRecordDO.getEffectiveTime() == null && dataGrantParam.getEffectiveTime() != null){
			return true;
		}
		if(dataGrantRecordDO.getEffectiveTime() != null && !dataGrantRecordDO.getEffectiveTime().equals(dataGrantParam.getEffectiveTime())){
			return true;
		}
		
		if(dataGrantRecordDO.getExpireTime() == null && dataGrantParam.getExpireTime() != null){
			return true;
		}
		if(dataGrantRecordDO.getExpireTime() != null && !dataGrantRecordDO.getExpireTime().equals(dataGrantParam.getExpireTime())){
			return true;
		}
		return false;
	}
	
	private List<DataGrantRecordDetailDO> buildDataGrantRecordDetailDOs(long dataGrantRecordId, Map<String, PropertyValue> data, Map<String, DataPropertyDTO> propertyCodeMap){
		List<DataGrantRecordDetailDO> detailDOs = Lists.newArrayList();
		for(Map.Entry<String, PropertyValue> codeValueEntry : data.entrySet()){
			DataGrantRecordDetailDO detailDO = new DataGrantRecordDetailDO();
			String propertyCode = codeValueEntry.getKey();
			PropertyValue propertyValue = codeValueEntry.getValue();
			DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
			detailDO.setDataGrantRecordId(dataGrantRecordId);
			Date now = new Date();
			detailDO.setCreateTime(now);
			detailDO.setUpdateTime(now);
			detailDO.setPropertyId(dataPropertyDTO.getId());
			detailDO.setPropertyValue(propertyValue.getPropertyValue());
			detailDO.setPropertyValueDesc(propertyValue.getPropertyValueDesc());
			detailDOs.add(detailDO);
		}
		return detailDOs;
	}

	public boolean revokeDataPermission(DataRevokeParam dataRevokeParam) {
		String appKey = dataRevokeParam.getAppKey();
		String modelCode = dataRevokeParam.getModelCode();
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		
		String grantTargetId = dataRevokeParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		
		int grantTargetType = dataRevokeParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		if(grantTargetType == GrantTargetType.PERMISSION_GROUP.getCode()){
			long groupId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(groupId != 0, "grantTargetId非法");
			PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getPermissionGroupById(groupId);
			Assert.notNull(permissionGroupDTO, "权限组不存在：" + grantTargetId);
			Assert.isTrue(permissionGroupDTO.getAppKey().equals(appKey), "此权限组不属于app:" + appKey);
		}
		if(grantTargetType == GrantTargetType.ROLE.getCode()){//如果授权对象是角色，判断角色是否为空
			RoleDO roleDO = null;
			if(grantTargetId.contains("#")){
				String[] targets = grantTargetId.split("#");
				Assert.isTrue(targets.length == 2, "角色信息格式为appkey#role");
				roleDO = roleDAO.getRoleByKey(targets[0], targets[1]);
			}else{
				long roleId = NumberUtils.toLong(grantTargetId);
				Assert.isTrue(roleId > 0, "角色id必须为整数");
				roleDO = roleDAO.getRoleById(roleId);
			}
			Assert.notNull(roleDO, "角色不存在");
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "此角色不属于app:" + appKey);
		}
		
		String tenant = dataRevokeParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), modelCode);
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		
		DataGrantRecordParam dataGrantRecordParam = new DataGrantRecordParam();
		dataGrantRecordParam.setGrantTargetId(grantTargetId);
		dataGrantRecordParam.setGrantTargetType(grantTargetType);
		dataGrantRecordParam.setTenant(tenant);
		dataGrantRecordParam.setModelId(dataModelDTO.getId());
		
		String operationCode = dataRevokeParam.getOperationCode();
		List<Long> dataGrantRecordIds = dataRevokeParam.getDataGrantRecordIds();

		if(StringUtils.isBlank(operationCode) && CollectionUtil.isEmpty(dataGrantRecordIds)){//如果没有传入操作,且操作记录id为null，则回收模型下所有数据
			dataGrantRecordDAO.removeByParam(dataGrantRecordParam);
		}else if(StringUtils.isNotBlank(operationCode) && CollectionUtil.isEmpty(dataGrantRecordIds)){//回收模型操作下所有数据
			DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
			Assert.notNull(dataOperationDTO, operationCode + "对应的操作不存在");
			dataGrantRecordParam.setOperationId(dataOperationDTO.getId());
			dataGrantRecordDAO.removeByParam(dataGrantRecordParam);
		}else if(CollectionUtil.isNotEmpty(dataGrantRecordIds)){//回收模型dataGrantRecordIds对应的数据
			List<DataGrantRecordDO> dataGrantRecordDOs = dataGrantRecordDAO.getByIds(dataGrantRecordIds);
			Assert.notEmpty(dataGrantRecordDOs, "授权记录不存在");
			for(DataGrantRecordDO dataGrantRecordDO : dataGrantRecordDOs){
				Assert.isTrue(dataGrantRecordDO.getModelId() == dataModelDTO.getId(), "参数不合法");
				Assert.isTrue(dataGrantRecordDO.getGrantTargetId().equals(grantTargetId), "参数不合法");
				Assert.isTrue(dataGrantRecordDO.getGrantTargetType() == grantTargetType, "参数不合法");
				Assert.isTrue(dataGrantRecordDO.getTenant().equals(tenant), "参数不合法");
			}
			dataGrantRecordDAO.removeByIds(dataGrantRecordIds);
		}
		return true;
	}
	@Override
	public DatasCheckResult checkDatasPermission(DatasCheckParam datasCheckParam) {
		Assert.notNull(datasCheckParam, "参数不能为空");
		List<Map<String, String>> dataList = datasCheckParam.getDatas();
		Assert.notEmpty(dataList, "dataList不能为空");
		
		if(dataList.size() < 5){
			return checkDatasPermission0(datasCheckParam);
		}
		
		String grantTargetAppKey = datasCheckParam.getAppKey();//此appkey可能跟modelCode的appkey不同
		Assert.hasText(grantTargetAppKey, "appKey不能为空");
		
		String grantTargetId = datasCheckParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");

		int grantTargetType = datasCheckParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = datasCheckParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		String modelCode = datasCheckParam.getModelCode();
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		
		String operationCode = datasCheckParam.getOperationCode();
		DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
		
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), dataModelDTO.getCode());
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		Map<String, DataPropertyDTO> propertyCodeMap = new HashMap<String, DataPropertyDTO>();
		Map<Long, DataPropertyDTO> propertyIdMap = new HashMap<Long, DataPropertyDTO>();
		for(DataPropertyDTO dto : dataProperties){
			propertyCodeMap.put(dto.getCode(), dto);
			propertyIdMap.put(dto.getId(), dto);
		}
		
		for(Map<String, String> data : dataList){
			Map<String, String> dataForCheck = data;
			Assert.notEmpty(dataForCheck, "data不能为空");
			Assert.isTrue(dataProperties.size() == dataForCheck.size(), "属性列表与配置的属性列表数量不一致");
			
			//判断dataForCheck是否包含非法值		
			for(Map.Entry<String, String> codeValEntry : dataForCheck.entrySet()){
				String propertyCode = codeValEntry.getKey();
				String propertyValue = codeValEntry.getValue();
				DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "propertyCode非法");
				Assert.hasText(propertyValue, "属性值不能为空");
			}
		}
		
		DataGrantQueryParam dataGrantQueryParam = new DataGrantQueryParam();
		BeanUtils.copyProperties(datasCheckParam, dataGrantQueryParam);
		dataGrantQueryParam.setReturnNotEffective(false);
		List<DataGrantRecordDTO> dataGrantRecordDTOs = listDataGrantRecord(dataGrantQueryParam);
		DatasCheckResult datasCheckResult = new DatasCheckResult();
		boolean allSuccess = true;
		List<Boolean> resultDetails = new ArrayList<Boolean>();
		datasCheckResult.setResultDetails(resultDetails);
		if(CollectionUtil.isEmpty(dataGrantRecordDTOs)){
			datasCheckResult.setAllSuccess(false);
			for(int i = 0; i < dataList.size(); i++){
				resultDetails.add(false);
			}
			return datasCheckResult;
		}
		
		for(Map<String, String> data : dataList){
			boolean success = isDataValid(data, dataGrantRecordDTOs);
			if(!success){
				allSuccess = false;
			}
			resultDetails.add(success);
		}
		datasCheckResult.setAllSuccess(allSuccess);
		return datasCheckResult;
	}
	
	private boolean isDataValid(Map<String, String> data, List<DataGrantRecordDTO> dataGrantRecordDTOs){
		Date now = new Date();
		for(DataGrantRecordDTO dataGrantRecordDTO : dataGrantRecordDTOs){
			//判断记录是否失效
			Date effectiveTime = dataGrantRecordDTO.getEffectiveTime();
			Date expireTime = dataGrantRecordDTO.getExpireTime();
			if(effectiveTime != null && now.before(effectiveTime)){
				continue;
			}
			if(expireTime != null && now.after(expireTime)){
				continue;
			}
			boolean valid = true;
			Map<String, PropertyValue> grantedData = dataGrantRecordDTO.getData();
			for(Entry<String, PropertyValue> entry : grantedData.entrySet()){
				String propertyCode = entry.getKey();
				PropertyValue propertyValue = grantedData.get(propertyCode);
				String val = propertyValue.getPropertyValue();
				if(!val.equals(IceAclConstant.GRANTED_ALL_DATA_SYMBOL) && !val.equals(data.get(propertyCode))){
					valid = false;
				}
			}
			if(valid){
				return true;
			}
		}
		return false;
	}
	
	public DatasCheckResult checkDatasPermission0(DatasCheckParam datasCheckParam) {
		Assert.notNull(datasCheckParam, "参数不能为空");
		String grantTargetAppKey = datasCheckParam.getAppKey();//此appkey可能跟modelCode的appkey不同
		Assert.hasText(grantTargetAppKey, "appKey不能为空");
		
		String grantTargetId = datasCheckParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");

		int grantTargetType = datasCheckParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = datasCheckParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		String modelCode = datasCheckParam.getModelCode();
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		
		String operationCode = datasCheckParam.getOperationCode();
		DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
		
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), dataModelDTO.getCode());
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		Map<String, DataPropertyDTO> propertyCodeMap = Maps.newHashMap();
		Map<Long, DataPropertyDTO> propertyIdMap = Maps.newHashMap();
		for(DataPropertyDTO dto : dataProperties){
			propertyCodeMap.put(dto.getCode(), dto);
			propertyIdMap.put(dto.getId(), dto);
		}
		
		List<Map<String, String>> datas = datasCheckParam.getDatas();
		Assert.notEmpty(datas, "datas不能为空");
		Assert.isTrue(datas.size() <= 100, "每次最多验证100条数据");
		
		for(Map<String, String> data : datas){
			Map<String, String> dataForCheck = data;
			Assert.notEmpty(dataForCheck, "data不能为空");
			Assert.isTrue(dataProperties.size() == dataForCheck.size(), "属性列表与配置的属性列表数量不一致");
			
			//判断dataForCheck是否包含非法值		
			for(Map.Entry<String, String> codeValEntry : dataForCheck.entrySet()){
				String propertyCode = codeValEntry.getKey();
				String propertyValue = codeValEntry.getValue();
				DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "propertyCode非法");
				Assert.hasText(propertyValue, "属性值不能为空");
			}
		}
		DatasCheckResult datasCheckResult = new DatasCheckResult();
		List<Boolean> resultDetails = Lists.newArrayList();
		boolean allSuccess = true;
		for(Map<String, String> data : datas){
			DataCheckParam dataCheckParam = new DataCheckParam();
			BeanUtils.copyProperties(datasCheckParam, dataCheckParam);
			dataCheckParam.setData(data);
			boolean result = checkDataPermission(dataCheckParam, dataModelDTO, dataOperationDTO, propertyCodeMap);
			resultDetails.add(result);
			if(!result){
				allSuccess = false;
			}
		}
		datasCheckResult.setAllSuccess(allSuccess);
		datasCheckResult.setResultDetails(resultDetails);
		return datasCheckResult;
	}

	private boolean checkDataPermission(DataCheckParam dataCheckParam, DataModelDTO dataModelDTO, DataOperationDTO dataOperationDTO, Map<String, DataPropertyDTO> propertyCodeMap){
		String grantTargetAppKey = dataCheckParam.getAppKey();//此appkey可能跟modelCode的appkey不同
		Assert.hasText(grantTargetAppKey, "appKey不能为空");
		String grantTargetId = dataCheckParam.getGrantTargetId();
		int grantTargetType = dataCheckParam.getGrantTargetType();
		String tenant = dataCheckParam.getTenant();
		List<String> grantTargetIds = Lists.newArrayList();
		grantTargetIds.add(grantTargetId);
		if(grantTargetType == GrantTargetType.USER.getCode()){//校验用户是否有数据权限
			//校验直接授权给用户的数据;
			boolean rst = checkDataPermission0(dataCheckParam, grantTargetIds, dataModelDTO, dataOperationDTO, propertyCodeMap);
			if(rst){//如果有数据，直接返回
				return true;
			}
			
			//获取用户拥有有数据的角色
			if(dataCheckParam.isHierarchicalRole()){
				List<String> userRoleIds = getUserRoleIds(grantTargetAppKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(userRoleIds)){
					dataCheckParam.setGrantTargetType(GrantTargetType.ROLE.getCode());
					rst = checkDataPermission0(dataCheckParam, userRoleIds, dataModelDTO, dataOperationDTO, propertyCodeMap);
					if(rst){
						return true;
					}
				}
			}
			List<String> userPermissionGroupIds = Lists.newArrayList();
			//获取用户通过角色的权限组
			if(dataCheckParam.isHierarchicalCheckRolePermissionGroup()){//是否关联校验权角色上关联的权限组
				List<String> groupIds = getUserRolePermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(groupIds)){
					userPermissionGroupIds.addAll(groupIds);
				}
			}
			//直接挂在用户上的权限组
			if(dataCheckParam.isHierarchicalCheckPermissionGroup()){//是否关联校验权用户权限组
				List<String> groupIds = getUserPermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(groupIds)){
					userPermissionGroupIds.addAll(groupIds);
				}
			}
			if(CollectionUtil.isNotEmpty(userPermissionGroupIds)){
				dataCheckParam.setGrantTargetType(GrantTargetType.PERMISSION_GROUP.getCode());
				rst = checkDataPermission0(dataCheckParam, userPermissionGroupIds, dataModelDTO, dataOperationDTO, propertyCodeMap);
				if(rst){
					return true;
				}
			}
			//全部校验失败，返回false
			return false;
		}
		return checkDataPermission0(dataCheckParam, grantTargetIds, dataModelDTO, dataOperationDTO, propertyCodeMap);
	}
	
	@Override
	public boolean checkDataPermission(DataCheckParam dataCheckParam) {
		Assert.notNull(dataCheckParam, "参数不能为空");
		String grantTargetAppKey = dataCheckParam.getAppKey();//此appkey可能跟modelCode的appkey不同
		Assert.hasText(grantTargetAppKey, "appKey不能为空");
		
		String grantTargetId = dataCheckParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");

		int grantTargetType = dataCheckParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = dataCheckParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		String modelCode = dataCheckParam.getModelCode();
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		
		String operationCode = dataCheckParam.getOperationCode();
		DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
		Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
		
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), dataModelDTO.getCode());
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		
		Map<String, String> dataForCheck = dataCheckParam.getData();
		Assert.notEmpty(dataForCheck, "data不能为空");
		Assert.isTrue(dataProperties.size() == dataForCheck.size(), "属性列表与配置的属性列表数量不一致");
		
		Map<String, DataPropertyDTO> propertyCodeMap = Maps.newHashMap();
		Map<Long, DataPropertyDTO> propertyIdMap = Maps.newHashMap();
		for(DataPropertyDTO dto : dataProperties){
			propertyCodeMap.put(dto.getCode(), dto);
			propertyIdMap.put(dto.getId(), dto);
		}
		
		//判断dataForCheck是否包含非法值		
		for(Map.Entry<String, String> codeValEntry : dataForCheck.entrySet()){
			String propertyCode = codeValEntry.getKey();
			String propertyValue = codeValEntry.getValue();
			DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
			Assert.notNull(dataPropertyDTO, "propertyCode非法");
			Assert.hasText(propertyValue, "属性值不能为空");
		}
		return this.checkDataPermission(dataCheckParam, dataModelDTO, dataOperationDTO, propertyCodeMap);
	}
	
	private boolean checkDataPermission0(DataCheckParam dataCheckParam, List<String> grantTargetIds, DataModelDTO dataModelDTO, DataOperationDTO dataOperationDTO, Map<String, DataPropertyDTO> propertyCodeMap) {
		Assert.notEmpty(grantTargetIds, "grantTargetId不能为空");
		
		int grantTargetType = dataCheckParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String appKey = dataCheckParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		
		String tenant = dataCheckParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		Map<String, String> dataForCheck = dataCheckParam.getData();
		Map<Long, List<String>> propertyDatasMap = Maps.newHashMap();
		for(Map.Entry<String, String> codeValueEntry : dataForCheck.entrySet()){
			String propertyCode = codeValueEntry.getKey();
			String propertyValue = codeValueEntry.getValue();
			DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
			List<String> values = Lists.newArrayList();
			values.add(propertyValue);
			values.add(IceAclConstant.GRANTED_ALL_DATA_SYMBOL);
			propertyDatasMap.put(dataPropertyDTO.getId(), values);
		}
		DataGrantPropertyValueParam dataGrantPropertyValueParam = new DataGrantPropertyValueParam(); 
		dataGrantPropertyValueParam.setGrantTargetIds(grantTargetIds);
		dataGrantPropertyValueParam.setGrantTargetType(grantTargetType);
		dataGrantPropertyValueParam.setModelId(dataModelDTO.getId());
		dataGrantPropertyValueParam.setOperationId(dataOperationDTO.getId());
		dataGrantPropertyValueParam.setTenant(tenant);
		dataGrantPropertyValueParam.setPropertyValuesMap(propertyDatasMap);
		dataGrantPropertyValueParam.setReturnNotEffective(false);
		List<Long> recordIds = dataGrantRecordDAO.listIdByPropertyValues(dataGrantPropertyValueParam);
		if(CollectionUtil.isNotEmpty(recordIds)){
			return true;
		}
		return false;
	}

	private DataGrantPropertyValueParam buildDataGrantPropertyValueParam(DataGrantQueryParam dataGrantQueryParam){
		String appKey = dataGrantQueryParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		
		String modelCode = dataGrantQueryParam.getModelCode();
		Assert.hasText(modelCode, "modelCode不能为空");
		
		DataModelDTO dataModelDTO = dataModelManager.getDataModel(modelCode);
		Assert.notNull(dataModelDTO, "没查到modelCode对用的模型");
		
		DataGrantPropertyValueParam dataGrantPropertyDataParam = new DataGrantPropertyValueParam();
		dataGrantPropertyDataParam.setModelId(dataModelDTO.getId());
		
		String operationCode = dataGrantQueryParam.getOperationCode();
		if(StringUtils.isNotBlank(operationCode)){
			DataOperationDTO dataOperationDTO = dataOperationManager.getDataOperation(dataModelDTO.getId(), operationCode);
			Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
			dataGrantPropertyDataParam.setOperationId(dataOperationDTO.getId());
		}
		
		String grantTargetId = dataGrantQueryParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		List<String> grantTargetIds = Lists.newArrayList();
		grantTargetIds.add(grantTargetId);
		dataGrantPropertyDataParam.setGrantTargetIds(grantTargetIds);

		int grantTargetType = dataGrantQueryParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "grantTargetType非法");
		dataGrantPropertyDataParam.setGrantTargetType(grantTargetType);

		String tenant = dataGrantQueryParam.getTenant();
		Assert.hasText(tenant, "租户id不能为空");
		dataGrantPropertyDataParam.setTenant(tenant);
		
		Map<String, String> data = dataGrantQueryParam.getData();
		if(MapUtils.isNotEmpty(data)){
			List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(dataModelDTO.getAppKey(), modelCode);
			Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
			Map<String, DataPropertyDTO> propertyCodeMap = Maps.newHashMap();
			for(DataPropertyDTO dto : dataProperties){
				propertyCodeMap.put(dto.getCode(), dto);
			}
			Map<Long, List<String>> propertyValuesMap = Maps.newHashMap();
			for(Map.Entry<String, String> entry : data.entrySet()){
				String propertyCode = entry.getKey();
				String propertyValue = entry.getValue();
				Assert.hasText(propertyCode, "属性code不能为空");
				Assert.hasText(propertyValue, "属性值不能为空");
				DataPropertyDTO dataPropertyDTO = propertyCodeMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "属性code对应的属性不存在:" + propertyCode);
				List<String> values = Lists.newArrayList();
				values.add(propertyValue);
				values.add(IceAclConstant.GRANTED_ALL_DATA_SYMBOL);
				propertyValuesMap.put(dataPropertyDTO.getId(), values);
			}
			dataGrantPropertyDataParam.setPropertyValuesMap(propertyValuesMap);
		}
		dataGrantPropertyDataParam.setReturnNotEffective(dataGrantQueryParam.isReturnNotEffective());
		dataGrantPropertyDataParam.setPageNum(dataGrantQueryParam.getPageNum());
		dataGrantPropertyDataParam.setPageSize(dataGrantQueryParam.getPageSize());
		return dataGrantPropertyDataParam;
	}
	
	private Map<Long, Map<String, PropertyValue>> buildRecordDetailMap(List<Long> recordIds, Map<Long, DataPropertyDTO> propertyIdMap){
		List<DataGrantRecordDetailDO> recordDetails = dataGrantRecordDetailDAO.listDetailByRecordIds(recordIds);
		Assert.notEmpty(recordDetails, "数据库数据错误");
		Map<Long, Map<String, PropertyValue>> rstMap = Maps.newHashMap();
		for(DataGrantRecordDetailDO detail : recordDetails){
			Long id = detail.getDataGrantRecordId();
			Map<String, PropertyValue> codeValueMap = rstMap.get(id);
			if(codeValueMap == null){
				codeValueMap = Maps.newHashMap();
				rstMap.put(id, codeValueMap);
			}
			Long propertyId = detail.getPropertyId();
			DataPropertyDTO propertyDTO = propertyIdMap.get(propertyId);
			Assert.notNull(propertyDTO, "数据库数据错误,属性id" + detail.getPropertyId() + "没找到对应的数据");
			String propertyCode = propertyDTO.getCode();
			PropertyValue propertyValue = new PropertyValue();
			propertyValue.setPropertyValue(detail.getPropertyValue());
			propertyValue.setPropertyValueDesc(detail.getPropertyValueDesc());	
			codeValueMap.put(propertyCode, propertyValue);
		}
		return rstMap;
	}
	
	private List<DataGrantRecordDTO> buildDataGrantRecordVOs(List<DataGrantRecordDO> datas, Long modelId, String modelCode){
		List<DataGrantRecordDTO> items = Lists.newArrayList();
		if(CollectionUtil.isEmpty(datas)){
			return items;
		}
		List<DataPropertyDTO> dataProperties = dataModelPropertyRefManager.listDataProperty(modelId);
		Assert.notEmpty(dataProperties, "没有配置模型对应的属性");
		Map<Long, DataPropertyDTO> propertyIdMap = Maps.newHashMap();
		for(DataPropertyDTO propertyDTO : dataProperties){
			propertyIdMap.put(propertyDTO.getId(), propertyDTO);
		}
		List<Long> recordIds = Lists.newArrayList();
		for(DataGrantRecordDO dataGrantRecordDO : datas){
			recordIds.add(dataGrantRecordDO.getId());
		}
		Map<Long, Map<String, PropertyValue>> recordDetailMap = this.buildRecordDetailMap(recordIds, propertyIdMap);
		List<DataOperationDTO> operationDTOs =  dataOperationManager.listDataOperation(modelId);
		Assert.notEmpty(operationDTOs, "没有配置模型对应的操作");
		Map<Long, DataOperationDTO> operationMap = Maps.newHashMap();
		for(DataOperationDTO operationDTO : operationDTOs){
			operationMap.put(operationDTO.getId(), operationDTO);
		}
		for(DataGrantRecordDO dataGrantRecordDO : datas){
			DataGrantRecordDTO dataGrantRecordDTO = new DataGrantRecordDTO();
			BeanUtils.copyProperties(dataGrantRecordDO, dataGrantRecordDTO);
			dataGrantRecordDTO.setModelCode(modelCode);
			DataOperationDTO operationDTO = operationMap.get(dataGrantRecordDO.getOperationId());
			Assert.notNull(operationDTO, "操作不存在：" + dataGrantRecordDO.getOperationId());
			dataGrantRecordDTO.setOperationCode(operationDTO.getCode());
			dataGrantRecordDTO.setOperationName(operationDTO.getName());
			dataGrantRecordDTO.setData(recordDetailMap.get(dataGrantRecordDO.getId()));
			items.add(dataGrantRecordDTO);
		}
		return items;
	}
	
	@Override
	public List<DataGrantRecordDTO> listDataGrantRecord(DataGrantQueryParam dataGrantQueryParam) {
		Assert.notNull(dataGrantQueryParam, "参数不能为空");
		boolean hierarchicalRole = dataGrantQueryParam.isHierarchicalRole();
		boolean hierarchicalPermissionGroup = dataGrantQueryParam.isHierarchicalPermissionGroup();
		boolean hierarchicalRolePermissionGroup = dataGrantQueryParam.isHierarchicalRolePermissionGroup();
		List<DataGrantRecordDO> datas = Lists.newArrayList();
		DataGrantPropertyValueParam dataGrantPropertyValueParam = buildDataGrantPropertyValueParam(dataGrantQueryParam);
		String grantTargetId = dataGrantQueryParam.getGrantTargetId();
		String tenant = dataGrantQueryParam.getTenant();
		String grantTargetAppKey = dataGrantQueryParam.getAppKey();//此appkey可能跟modelCode的appkey不同
		Assert.hasText(grantTargetAppKey, "appKey不能为空");
		int grantTargetType = dataGrantQueryParam.getGrantTargetType();
		if(MapUtils.isNotEmpty(dataGrantQueryParam.getData())){//根据属性值查询
			List<Long> recordIds = dataGrantRecordDAO.listIdByPropertyValues(dataGrantPropertyValueParam);
			if(recordIds == null){
				recordIds = Lists.newArrayList();
			}
			if(GrantTargetType.USER.getCode() == grantTargetType){
				if(hierarchicalRole){
					//获取到用户拥有的角色
					List<String> userRoleIds = getUserRoleIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(userRoleIds)){
						dataGrantPropertyValueParam.setGrantTargetType(GrantTargetType.ROLE.getCode());
						dataGrantPropertyValueParam.setGrantTargetIds(userRoleIds);
						List<Long> roleRecordIds = dataGrantRecordDAO.listIdByPropertyValues(dataGrantPropertyValueParam);
						if(CollectionUtil.isNotEmpty(roleRecordIds)){
							recordIds.addAll(roleRecordIds);
						}
					}
				}
				//获取到用户拥有的权限组
				List<String> userPermissionGroupIds = Lists.newArrayList();
				if(hierarchicalRolePermissionGroup){
					List<String> groupIds = getUserRolePermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(groupIds)){
						userPermissionGroupIds.addAll(groupIds);
					}
				}
				if(hierarchicalPermissionGroup){
					List<String> groupIds = getUserPermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(groupIds)){
						userPermissionGroupIds.addAll(groupIds);
					}
				}
				if(CollectionUtil.isNotEmpty(userPermissionGroupIds)){
					dataGrantPropertyValueParam.setGrantTargetType(GrantTargetType.PERMISSION_GROUP.getCode());
					dataGrantPropertyValueParam.setGrantTargetIds(userPermissionGroupIds);
					List<Long> roleRecordIds = dataGrantRecordDAO.listIdByPropertyValues(dataGrantPropertyValueParam);
					if(CollectionUtil.isNotEmpty(roleRecordIds)){
						recordIds.addAll(roleRecordIds);
					}
				}
			}
			if(CollectionUtil.isNotEmpty(recordIds)){
				datas.addAll(dataGrantRecordDAO.getByIds(recordIds));
			}
		}else{
			datas = dataGrantRecordDAO.listDataGrantRecord(dataGrantPropertyValueParam);
			if(GrantTargetType.USER.getCode() == grantTargetType){
				if(hierarchicalRole){
					//获取到用户拥有的角色
					List<String> userRoleIds = getUserRoleIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(userRoleIds)){
						dataGrantPropertyValueParam.setGrantTargetType(GrantTargetType.ROLE.getCode());
						dataGrantPropertyValueParam.setGrantTargetIds(userRoleIds);
						List<DataGrantRecordDO> roleDatas = dataGrantRecordDAO.listDataGrantRecord(dataGrantPropertyValueParam);
						if(CollectionUtil.isNotEmpty(roleDatas)){
							datas.addAll(roleDatas);
						}
					}
				}
				//获取到用户拥有的权限组
				List<String> userPermissionGroupIds = Lists.newArrayList();
				if(hierarchicalRolePermissionGroup){
					List<String> groupIds = getUserRolePermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(groupIds)){
						userPermissionGroupIds.addAll(groupIds);
					}
				}
				if(hierarchicalPermissionGroup){
					List<String> groupIds = getUserPermissionGroupIds(grantTargetAppKey, grantTargetId, tenant);
					if(CollectionUtil.isNotEmpty(groupIds)){
						userPermissionGroupIds.addAll(groupIds);
					}
				}
				if(CollectionUtil.isNotEmpty(userPermissionGroupIds)){
					dataGrantPropertyValueParam.setGrantTargetType(GrantTargetType.PERMISSION_GROUP.getCode());
					dataGrantPropertyValueParam.setGrantTargetIds(userPermissionGroupIds);
					List<DataGrantRecordDO> permissionGroupDatas = dataGrantRecordDAO.listDataGrantRecord(dataGrantPropertyValueParam);
					if(CollectionUtil.isNotEmpty(permissionGroupDatas)){
						datas.addAll(permissionGroupDatas);
					}
				}
			}
		}
		return buildDataGrantRecordVOs(datas, dataGrantPropertyValueParam.getModelId(), dataGrantQueryParam.getModelCode());
	}

	@Override
	public PageVO<DataGrantRecordDTO> pageDataGrantRecord(DataGrantQueryParam dataGrantQueryParam) {
		Assert.notNull(dataGrantQueryParam, "参数不能为空");
		PageVO<DataGrantRecordDTO> pageVO = new PageVO<DataGrantRecordDTO>(dataGrantQueryParam);
		List<DataGrantRecordDO> datas = null;
		DataGrantPropertyValueParam dataGrantPropertyValueParam = buildDataGrantPropertyValueParam(dataGrantQueryParam);
		dataGrantPropertyValueParam.setReturnNotEffective(true);
		
		if(MapUtils.isNotEmpty(dataGrantQueryParam.getData())){//根据属性值查询
    		if(dataGrantQueryParam.isReturnTotalCount()){
	    		long total = dataGrantRecordDAO.countIdByPropertyValues(dataGrantPropertyValueParam);
	    		pageVO.setTotal(total);
	    		if(total == 0){
	    			return pageVO;
	    		}
    		}
    		List<Long> recordIds = dataGrantRecordDAO.pageIdByPropertyValues(dataGrantPropertyValueParam);
    		datas = dataGrantRecordDAO.getByIds(recordIds);
    	}else{
    		if(dataGrantQueryParam.isReturnTotalCount()){
	    		long total = dataGrantRecordDAO.countDataGrantRecord(dataGrantPropertyValueParam);
	    		pageVO.setTotal(total);
	    		if(total == 0){
	    			return pageVO;
	    		}
    		}
    		datas = dataGrantRecordDAO.pageDataGrantRecord(dataGrantPropertyValueParam);
    	}
    	List<DataGrantRecordDTO> items = buildDataGrantRecordVOs(datas, dataGrantPropertyValueParam.getModelId(), dataGrantQueryParam.getModelCode());
		pageVO.setItems(items);
		return pageVO;
	}
	
	private List<String> getUserRoleIds(String appKey, String userId, String tenant){
		List<String> userRoleIds = Lists.newArrayList();
		//查出用户所有的角色
		UserRoleRefQueryParam param = new UserRoleRefQueryParam();
		param.setAppKey(appKey);
		param.setTenant(tenant);
		param.setUserId(userId);
		param.setReturnNotEffective(false);
		List<UserRoleRefVO> userRoleRefVOs = userRoleRefDAO.listUserRoleRef(param);
		if(CollectionUtil.isEmpty(userRoleRefVOs)){
			return userRoleIds;//用户没有角色，直接返回失败
		}
		for(UserRoleRefVO userRoleRefVO : userRoleRefVOs){
			userRoleIds.add(String.valueOf(userRoleRefVO.getRoleId()));
		}
		return userRoleIds;
	}
	
	private List<String> getUserRolePermissionGroupIds(String appKey, String userId, String tenant){
		List<String> groupIds = Lists.newArrayList();
		List<Long> permissionGroupIds = rolePermissionGroupRefManager.listUserRolePermissionGroupIds(userId, appKey, tenant);
		if(CollectionUtil.isEmpty(permissionGroupIds)){
			return groupIds;
		}
		for(Long permissionGroupId : permissionGroupIds){
			groupIds.add(String.valueOf(permissionGroupId));
		}
		return groupIds;
	}
	
	private List<String> getUserPermissionGroupIds(String appKey, String userId, String tenant){
		List<String> groupIds = Lists.newArrayList();
		UserPermissionGroupRefQueryParam param = new UserPermissionGroupRefQueryParam();
		param.setUserId(userId);
		param.setAppKey(appKey);
		param.setTenant(tenant);
		param.setReturnNotEffective(false);
		List<Long> permissionGroupIds = userPermissionGroupRefManager.listUserPermissionGroupIds(param);
		if(CollectionUtil.isEmpty(permissionGroupIds)){
			return groupIds;
		}
		for(Long permissionGroupId : permissionGroupIds){
			groupIds.add(String.valueOf(permissionGroupId));
		}
		return groupIds;
	}
}
