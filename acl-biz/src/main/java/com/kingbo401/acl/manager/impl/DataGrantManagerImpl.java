package com.kingbo401.acl.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kingbo401.acl.common.constant.AclConstant;
import com.kingbo401.acl.common.enums.Comparator;
import com.kingbo401.acl.common.enums.GrantTargetType;
import com.kingbo401.acl.common.model.DataGrantRecordInfo;
import com.kingbo401.acl.common.model.PropertyRule;
import com.kingbo401.acl.common.model.ValueTextPair;
import com.kingbo401.acl.common.model.dto.DataGrantRecordDTO;
import com.kingbo401.acl.common.model.dto.DataModelDTO;
import com.kingbo401.acl.common.model.dto.DataOperationDTO;
import com.kingbo401.acl.common.model.dto.DataPropertyDTO;
import com.kingbo401.acl.common.model.dto.DatasCheckResult;
import com.kingbo401.acl.common.model.dto.PermissionGroupDTO;
import com.kingbo401.acl.common.model.dto.UserRoleRefDTO;
import com.kingbo401.acl.common.model.dto.param.DataCheckParam;
import com.kingbo401.acl.common.model.dto.param.DataGrantParam;
import com.kingbo401.acl.common.model.dto.param.DataGrantQueryParam;
import com.kingbo401.acl.common.model.dto.param.DataRevokeParam;
import com.kingbo401.acl.common.model.dto.param.DatasCheckParam;
import com.kingbo401.acl.dao.DataGrantRecordDAO;
import com.kingbo401.acl.dao.RoleDAO;
import com.kingbo401.acl.dao.UserRoleRefDAO;
import com.kingbo401.acl.manager.DataGrantManager;
import com.kingbo401.acl.manager.DataModelManager;
import com.kingbo401.acl.manager.DataOperationManager;
import com.kingbo401.acl.manager.DataPropertyManager;
import com.kingbo401.acl.manager.PermissionGroupManager;
import com.kingbo401.acl.manager.RolePermissionGroupRefManager;
import com.kingbo401.acl.manager.UserPermissionGroupRefManager;
import com.kingbo401.acl.model.entity.DataGrantRecordDO;
import com.kingbo401.acl.model.entity.RoleDO;
import com.kingbo401.acl.model.entity.param.DataGrantRecordParam;
import com.kingbo401.acl.model.entity.param.DataGrantRecordQueryParam;
import com.kingbo401.acl.model.entity.param.UserPermissionGroupRefQueryParam;
import com.kingbo401.acl.model.entity.param.UserRoleRefQueryParam;
import com.kingbo401.acl.util.BizUtil;
import com.kingbo401.commons.model.PageVO;
import com.kingbo401.commons.util.CollectionUtil;

@Service
public class DataGrantManagerImpl implements DataGrantManager{
	@Autowired
	private DataGrantRecordDAO dataGrantRecordDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private UserRoleRefDAO userRoleRefDAO;
	@Autowired
	private DataModelManager dataModelManager;
	@Autowired
	private DataOperationManager dataOperationManager;
	@Autowired
	private DataPropertyManager dataPropertyManager;
	@Autowired
	private RolePermissionGroupRefManager rolePermissionGroupRefManager;
	@Autowired
	private UserPermissionGroupRefManager userPermissionGroupRefManager;
	@Autowired
	private PermissionGroupManager permissionGroupManager;
	
	private Map<String, DataOperationDTO> getOperationMap(Long modelId){
		List<DataOperationDTO> operations = dataOperationManager.listDataOperation(modelId);
		if (CollectionUtils.isEmpty(operations)) {
			return new HashMap<>();
		}
		return operations.stream()
				.collect(Collectors.toMap(DataOperationDTO::getCode, a -> a, (k1, k2) -> k1));
	}
	
	private Map<String, DataPropertyDTO> getPropertyMap(Long modelId){
		List<DataPropertyDTO> properties = dataPropertyManager.listDataProperty(modelId);
		if (CollectionUtils.isEmpty(properties)) {
			return new HashMap<>();
		}
		return properties.stream()
				.collect(Collectors.toMap(DataPropertyDTO::getCode, a -> a, (k1, k2) -> k1));
	}
    
	@Override
	public boolean grantDataPermission(DataGrantParam dataGrantParam) {
		String appKey = dataGrantParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		
		String grantTargetId = dataGrantParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		
		int grantTargetType = dataGrantParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = dataGrantParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		if(grantTargetType == GrantTargetType.PERMISSION_GROUP.getCode()){
			long groupId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(groupId != 0, "grantTargetId非法");
			PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getById(groupId);
			Assert.notNull(permissionGroupDTO, "权限组不存在：" + grantTargetId);
			Assert.isTrue(permissionGroupDTO.getAppKey().equals(appKey), "此权限组不属于app:" + appKey);
		}
		
		if(grantTargetType == GrantTargetType.ROLE.getCode()){
			long roleId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(roleId > 0, "角色id必须为整数");
			RoleDO roleDO = roleDAO.getById(roleId);
			Assert.notNull(roleDO, "角色不存在");
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "此角色不属于app:" + appKey);
		}
		
		//判断数据是否包含非法值
		List<DataGrantRecordInfo> records = dataGrantParam.getRecords();
		Assert.notEmpty(records, "records不能为空");
		List<String> modelCodes = records.stream().map(DataGrantRecordInfo::getModelCode).distinct().collect(Collectors.toList());
		List<DataModelDTO> models = dataModelManager.getByCodes(modelCodes);
		Assert.notEmpty(models, "modelCode非法");
		Assert.isTrue(modelCodes.size() == models.size(), "modelCode非法.");
		Map<String, DataModelDTO> modelMap = new HashMap<>();
		Map<Long, Map<String, DataPropertyDTO>> modelPropertyMap = new HashMap<>();
		Map<Long, Map<String, DataOperationDTO>> modelOperationMap = new HashMap<>();
		models.forEach(model -> {
			String modelCode = model.getCode();
			Long modelId = model.getId();
			modelMap.put(modelCode, model);
			modelPropertyMap.put(modelId, this.getPropertyMap(modelId));
			modelOperationMap.put(modelId, this.getOperationMap(modelId));
		});
		records.forEach(record -> {
			String modelCode = record.getModelCode();
			DataModelDTO model = modelMap.get(modelCode);
			Assert.notNull(model, "模型不存在, code:" + modelCode);
			Long modelId = model.getId();
			BizUtil.checkEffectiveExpireTime(record.getEffectiveTime(), record.getExpireTime());
			Set<String> operationCodes = record.getOperationCodes();
			Assert.notEmpty(operationCodes, "operationCodes不能为空");
			Map<String, DataOperationDTO> operationMap = modelOperationMap.get(modelId);
			operationCodes.forEach(operatinCode -> {
				Assert.notNull(operationMap.get(operatinCode), "operationCode不存在：" + operatinCode);
			});
			Set<PropertyRule> propertyRules = record.getPropertyRules();
			Assert.notEmpty(propertyRules, "propertyRules不能为空");
			Map<String, DataPropertyDTO> propertyMap = modelPropertyMap.get(modelId);
			propertyRules.forEach(propertyRule -> {
				String code = propertyRule.getCode();
				String comparator = propertyRule.getComparator();
				Assert.hasText(code, "code不能为空");
				DataPropertyDTO dataPropertyDTO = propertyMap.get(code);
				Assert.notNull(dataPropertyDTO, "属性编码不存在：" + code);
				Assert.hasText(comparator, "comparator不能为空");
				Assert.isTrue(Comparator.isValid(comparator), "比较符非法");
				List<ValueTextPair> values = propertyRule.getValues();
				values.forEach(pair -> {
					String value = pair.getValue();
					String text = pair.getText();
					Assert.hasText(value, "value不能为空");
					Assert.hasText(text, "text不能为空");
					//TODO 判断value类型合法性
				});
			});
		});
		records.forEach(record -> {
			String modelCode = record.getModelCode();
			DataModelDTO model = modelMap.get(modelCode);
			Long modelId = model.getId();
			Long id = record.getId();
			DataGrantRecordDO dataGrantRecordDO = new DataGrantRecordDO();
			dataGrantRecordDO.setId(id);
			dataGrantRecordDO.setAppKey(appKey);
			dataGrantRecordDO.setModelId(modelId);
			dataGrantRecordDO.setOperationCodes(JSON.toJSONString(record.getOperationCodes()));
			dataGrantRecordDO.setPropertyRules(JSON.toJSONString(record.getPropertyRules()));
			dataGrantRecordDO.setStatus(AclConstant.STATUS_NORMAL);
			dataGrantRecordDO.setEffectiveTime(record.getEffectiveTime());
			dataGrantRecordDO.setExpireTime(record.getExpireTime());
			dataGrantRecordDO.setGrantTargetId(grantTargetId);
			dataGrantRecordDO.setGrantTargetType(grantTargetType);
			dataGrantRecordDO.setTenant(tenant);
			if (id != null && id != 0) {
				DataGrantRecordDO orignal = dataGrantRecordDAO.getById(id);
				Assert.notNull(orignal, "授权记录不存在");
				Assert.isTrue(appKey.equals(orignal.getAppKey())
						&& modelId.equals(orignal.getModelId())
						&& grantTargetId.equals(orignal.getGrantTargetId())
						&& grantTargetType == orignal.getGrantTargetType()
						&& tenant.equals(orignal.getTenant()), "授权记录ID非法");
				dataGrantRecordDAO.update(dataGrantRecordDO);
			} else {
				dataGrantRecordDAO.create(dataGrantRecordDO);
			}
		});
		return true;
	}

	public boolean revokeDataPermission(DataRevokeParam dataRevokeParam) {
		String appKey = dataRevokeParam.getAppKey();		
		Assert.hasText(appKey, "appKey不能为空");

		String modelCode = dataRevokeParam.getModelCode();
		Long modelId = null;
		if (StringUtils.isNotBlank(modelCode)) {
			DataModelDTO dataModelDTO = dataModelManager.getByCode(modelCode);
			Assert.notNull(dataModelDTO, "modelCode非法");
			modelId = dataModelDTO.getId();
		}
		
		String tenant = dataRevokeParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		String grantTargetId = dataRevokeParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		
		int grantTargetType = dataRevokeParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		if(grantTargetType == GrantTargetType.PERMISSION_GROUP.getCode()){
			long groupId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(groupId != 0, "grantTargetId非法");
			PermissionGroupDTO permissionGroupDTO = permissionGroupManager.getById(groupId);
			Assert.notNull(permissionGroupDTO, "权限组不存在：" + grantTargetId);
			Assert.isTrue(permissionGroupDTO.getAppKey().equals(appKey), "此权限组不属于app:" + appKey);
		}
		//如果授权对象是角色，判断角色是否为空
		if(grantTargetType == GrantTargetType.ROLE.getCode()){
			long roleId = NumberUtils.toLong(grantTargetId);
			Assert.isTrue(roleId > 0, "角色id必须为整数");
			RoleDO roleDO = roleDAO.getById(roleId);
			Assert.notNull(roleDO, "角色不存在");
			Assert.isTrue(roleDO.getAppKey().equals(appKey), "此角色不属于app:" + appKey);
		}
		
		DataGrantRecordParam dataGrantRecordParam = new DataGrantRecordParam();
		dataGrantRecordParam.setAppKey(appKey);
		dataGrantRecordParam.setGrantTargetId(grantTargetId);
		dataGrantRecordParam.setGrantTargetType(grantTargetType);
		dataGrantRecordParam.setTenant(tenant);
		dataGrantRecordParam.setModelId(modelId);
		
		List<Long> dataGrantRecordIds = dataRevokeParam.getDataGrantRecordIds();
		
		//如果操作记录id为空，则回收模型下所有数据
		if(CollectionUtil.isEmpty(dataGrantRecordIds)){
			dataGrantRecordDAO.removeByParam(dataGrantRecordParam);
		} else{
			//回收模型dataGrantRecordIds对应的数据
			List<DataGrantRecordDO> dataGrantRecordDOs = dataGrantRecordDAO.getByIds(dataGrantRecordIds);
			Assert.notEmpty(dataGrantRecordDOs, "授权记录不存在");
			for(DataGrantRecordDO dataGrantRecordDO : dataGrantRecordDOs){
				Assert.isTrue(dataGrantRecordDO.getAppKey().equals(appKey), "参数非法");
				if (modelId != null) {
					Assert.isTrue(dataGrantRecordDO.getModelId().equals(modelId), "参数非法");
				}
				Assert.isTrue(dataGrantRecordDO.getGrantTargetId().equals(grantTargetId), "参数非法");
				Assert.isTrue(dataGrantRecordDO.getGrantTargetType() == grantTargetType, "参数非法");
				Assert.isTrue(dataGrantRecordDO.getTenant().equals(tenant), "参数非法");
			}
			dataGrantRecordDAO.removeByIds(dataGrantRecordIds);
		}
		return true;
	}
	
	@Override
	public DatasCheckResult checkDatasPermission(DatasCheckParam datasCheckParam) {
		//此appkey可能跟modelCode的appkey不同
		String appKey = datasCheckParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		
		String grantTargetId = datasCheckParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");

		int grantTargetType = datasCheckParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "授权目标类型非法");
		
		String tenant = datasCheckParam.getTenant();
		Assert.hasText(tenant, "tenant不能为空");
		
		String modelCode = datasCheckParam.getModelCode();
		DataModelDTO dataModelDTO = dataModelManager.getByCode(modelCode);
		Assert.notNull(dataModelDTO, "modelCode非法");
		Long modelId = dataModelDTO.getId();
		
 		String operationCode = datasCheckParam.getOperationCode();
 		if (!AclConstant.ALL_DATA.equals(operationCode)) {
 			DataOperationDTO dataOperationDTO = dataOperationManager.getByCode(modelId, operationCode);
 			Assert.notNull(dataOperationDTO, "没查到operationCode对用的操作");
		}
		
		Map<String, DataPropertyDTO> propertyMap = this.getPropertyMap(modelId);
		Map<String, DataOperationDTO> operationMap = this.getOperationMap(modelId);
		List<Map<String, String>> datas = datasCheckParam.getDatas();
		Assert.notEmpty(datas, "datas不能为空");
		
		for(Map<String, String> data : datas){
			Assert.notEmpty(data, "data不能为空");
			Assert.isTrue(propertyMap.size() == data.size(), "属性列表与配置的属性列表数量不一致");
			
			//判断data是否包含非法值		
			for(Map.Entry<String, String> codeValEntry : data.entrySet()){
				String propertyCode = codeValEntry.getKey();
				String propertyValue = codeValEntry.getValue();
				DataPropertyDTO dataPropertyDTO = propertyMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "propertyCode非法");
				Assert.hasText(propertyValue, "属性值不能为空");
			}
		}
		
		List<String> grantTargetIds = new ArrayList<>();
		grantTargetIds.add(grantTargetId);
		DataGrantRecordQueryParam dataGrantRecordQueryParam = new DataGrantRecordQueryParam();
		dataGrantRecordQueryParam.setAppKey(appKey);
		dataGrantRecordQueryParam.setTenant(tenant);
		dataGrantRecordQueryParam.setGrantTargetIds(grantTargetIds);
		dataGrantRecordQueryParam.setGrantTargetType(grantTargetType);
		dataGrantRecordQueryParam.setModelId(modelId);
		dataGrantRecordQueryParam.setReturnNotEffective(false);
		dataGrantRecordQueryParam.setReturnTotalCount(false);
		//校验用户是否有数据权限
		if (grantTargetType == GrantTargetType.USER.getCode()) {
			//校验直接授权给用户的数据
			List<DataGrantRecordDTO> dataGrantRecordDTOs 
				= this.buildDataGrantRecordDTOs(dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam), modelCode, propertyMap, operationMap);
			DatasCheckResult result = this.checkDatas(operationCode, datas, dataGrantRecordDTOs);
			if (result.isAllSuccess()) {
				return result;
			}
			if (datasCheckParam.isHierarchicalCheckRole()) {
				List<String> roleIds = this.getUserRoleIds(appKey, grantTargetId, tenant);
				if (CollectionUtils.isNotEmpty(roleIds)) {
					dataGrantRecordQueryParam.setGrantTargetType(GrantTargetType.ROLE.getCode());
					dataGrantRecordQueryParam.setGrantTargetIds(roleIds);
					dataGrantRecordDTOs 
						= this.buildDataGrantRecordDTOs(dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam), modelCode, propertyMap, operationMap);
					result = this.checkDatas(operationCode, datas, dataGrantRecordDTOs);
					if (result.isAllSuccess()) {
						return result;
					}
				}
			}
			List<String> groupIds = new ArrayList<>();
			if (datasCheckParam.isHierarchicalCheckPermissionGroup()) {
				List<String> list = this.getUserPermissionGroupIds(appKey, grantTargetId, tenant);
				if (CollectionUtils.isNotEmpty(list)) {
					groupIds.addAll(list);
				}		
			}
			if (datasCheckParam.isHierarchicalCheckRolePermissionGroup()) {
				List<String> list = this.getUserRolePermissionGroupIds(appKey, grantTargetId, tenant);
				if (CollectionUtils.isNotEmpty(list)) {
					groupIds.addAll(list);
				}	
			}
			if (CollectionUtils.isNotEmpty(groupIds)) {
				dataGrantRecordQueryParam.setGrantTargetType(GrantTargetType.PERMISSION_GROUP.getCode());
				dataGrantRecordQueryParam.setGrantTargetIds(groupIds);
				dataGrantRecordDTOs 
					= this.buildDataGrantRecordDTOs(dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam), modelCode, propertyMap, operationMap);
				result = this.checkDatas(operationCode, datas, dataGrantRecordDTOs);
				if (result.isAllSuccess()) {
					return result;
				}
			}
		}
		List<DataGrantRecordDTO> dataGrantRecordDTOs 
			= this.buildDataGrantRecordDTOs(dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam), modelCode, propertyMap, operationMap);
		return this.checkDatas(operationCode, datas, dataGrantRecordDTOs);
	}
	
	private DatasCheckResult checkDatas(String operationCode, List<Map<String, String>> datas, List<DataGrantRecordDTO> dataGrantRecordDTOs){
		DatasCheckResult result = new DatasCheckResult();
		result.setAllSuccess(false);
		List<Boolean> resultDetails = datas.stream().map(item -> false).collect(Collectors.toList());
		result.setResultDetails(resultDetails);
		if (CollectionUtils.isEmpty(dataGrantRecordDTOs)) {
			return result;
		}
		int i = 0;
		for (Map<String, String> data : datas) {
			if (this.checkData(operationCode, data, dataGrantRecordDTOs)) {
				resultDetails.set(i, true);
			}
			i++;
		}
		boolean allSuccess = true;
		for (boolean resultItem :  resultDetails) {
			if (!resultItem) {
				allSuccess = false;
				break;
			}
		}
		result.setAllSuccess(allSuccess);
		return result;
	}
	
	private boolean checkData(String operationCode, Map<String, String> data, List<DataGrantRecordDTO> dataGrantRecordDTOs) {
		if (CollectionUtils.isEmpty(dataGrantRecordDTOs)) {
			return false;
		}
		for (DataGrantRecordDTO dataGrantRecordDTO : dataGrantRecordDTOs) {
			if(this.checkData(operationCode, data, dataGrantRecordDTO, false)) {
				return true;
			}
		}
		return false;
	}
	
	private void filterDataGrantRecords(String operationCode, Map<String, String> data, List<DataGrantRecordDTO> dataGrantRecordDTOs) {
		if (MapUtils.isEmpty(data)) {
			return;
		}
		if (CollectionUtils.isEmpty(dataGrantRecordDTOs)) {
			return;
		}
		Iterator<DataGrantRecordDTO> iterator = dataGrantRecordDTOs.iterator();
		while (iterator.hasNext()) {
			//删除不匹配的记录
			if(!checkData(operationCode, data, iterator.next(), true)) {
				iterator.remove();
			}
		}
	}
	
	private boolean checkData(String operationCode, Map<String, String> data, DataGrantRecordDTO dataGrantRecordDTO, boolean filter) {
		List<String> operationCodes = dataGrantRecordDTO.getOperationCodes();
		if (CollectionUtils.isEmpty(operationCodes)) {
			return false;
		}
		if(operationCode != null 
				&& (!operationCodes.contains(operationCode) 
						|| !operationCodes.contains(AclConstant.ALL_DATA))) {
			return false;
		}
		boolean valid = true;
		List<PropertyRule> propertyRules = dataGrantRecordDTO.getPropertyRules();
		if (CollectionUtils.isEmpty(propertyRules)) {
			return false;
		}
		for (PropertyRule propertyRule : propertyRules) {
			String propertyCode = propertyRule.getCode();
			String comparator = propertyRule.getComparator();
			List<ValueTextPair> valueTextPairs = propertyRule.getValues();
			List<String> values = valueTextPairs.stream().map(ValueTextPair::getValue).collect(Collectors.toList());
			String propertyData = data.get(propertyCode);
			//如果是过滤数据且参数data中不存在当前属性值，比较下一条
			if(filter && propertyData == null) {
				continue;
			}
			//比较符为等号
			if(Comparator.EQ.isMe(comparator)) {
				if(!values.contains(AclConstant.ALL_DATA) && !values.contains(propertyData)) {
					valid = false;
					break;
				}
			}
			//TODO 支持其它比较符
		}
		return valid;
	}
	
	@Override
	public boolean checkDataPermission(DataCheckParam dataCheckParam) {
		Map<String, String> data = dataCheckParam.getData();
		Assert.notEmpty(data, "data不能为空");
		DatasCheckParam datasCheckParam = new DatasCheckParam();
		BeanUtils.copyProperties(dataCheckParam, datasCheckParam);
		List<Map<String, String>> datas = new ArrayList<>();
		datas.add(data);
		datasCheckParam.setDatas(datas);
		return this.checkDatasPermission(datasCheckParam).isAllSuccess();
	}

	private DataGrantRecordQueryParam buildDataGrantRecordQueryParam(DataGrantQueryParam dataGrantQueryParam){
		String appKey = dataGrantQueryParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		
		String modelCode = dataGrantQueryParam.getModelCode();
		Long modelId = null;
		if (StringUtils.isNotBlank(modelCode)) {
			DataModelDTO dataModelDTO = dataModelManager.getByCode(modelCode);
			Assert.notNull(dataModelDTO, "没查到modelCode对用的模型");
			modelId = dataModelDTO.getId();
		}
		
		DataGrantRecordQueryParam dataGrantRecordQueryParam = new DataGrantRecordQueryParam();
		dataGrantRecordQueryParam.setAppKey(appKey);
		dataGrantRecordQueryParam.setModelId(modelId);
		
		String grantTargetId = dataGrantQueryParam.getGrantTargetId();
		Assert.hasText(grantTargetId, "grantTargetId不能为空");
		List<String> grantTargetIds = Lists.newArrayList();
		grantTargetIds.add(grantTargetId);
		dataGrantRecordQueryParam.setGrantTargetIds(grantTargetIds);

		int grantTargetType = dataGrantQueryParam.getGrantTargetType();
		Assert.isTrue(GrantTargetType.isValid(grantTargetType), "grantTargetType非法");
		dataGrantRecordQueryParam.setGrantTargetType(grantTargetType);

		String tenant = dataGrantQueryParam.getTenant();
		Assert.hasText(tenant, "租户id不能为空");
		dataGrantRecordQueryParam.setTenant(tenant);
		
		Map<String, String> data = dataGrantQueryParam.getData();
		if (modelId == null && data != null) {
			data.clear();
		}
		if(MapUtils.isNotEmpty(data)){
			Map<String, DataPropertyDTO> propertyMap = this.getPropertyMap(modelId);
			for(Map.Entry<String, String> entry : data.entrySet()){
				String propertyCode = entry.getKey();
				String propertyValue = entry.getValue();
				Assert.hasText(propertyCode, "属性code不能为空");
				Assert.hasText(propertyValue, "属性值不能为空");
				DataPropertyDTO dataPropertyDTO = propertyMap.get(propertyCode);
				Assert.notNull(dataPropertyDTO, "属性code对应的属性不存在:" + propertyCode);
			}
		}
		dataGrantRecordQueryParam.setReturnNotEffective(dataGrantQueryParam.isReturnNotEffective());
		dataGrantRecordQueryParam.setPageNum(dataGrantQueryParam.getPageNum());
		dataGrantRecordQueryParam.setPageSize(dataGrantQueryParam.getPageSize());
		return dataGrantRecordQueryParam;
	}
	
	private DataGrantRecordDTO buildDataGrantRecordDTO(DataGrantRecordDO dataGrantRecordDO, String modelCode,
			Map<String, DataPropertyDTO> propertyMap, Map<String, DataOperationDTO> operationMap){
		String operationCodesStr = dataGrantRecordDO.getOperationCodes();
		String propertyRulesStr = dataGrantRecordDO.getPropertyRules();
		List<String> operationCodes = JSON.parseArray(operationCodesStr, String.class);
		List<DataOperationDTO> operations = new ArrayList<>();
		Iterator<String> opIter = operationCodes.iterator();
		while(opIter.hasNext()) {
			String operationCode = opIter.next();
			if (operationCode.equals(AclConstant.ALL_DATA)) {
				DataOperationDTO operation = new DataOperationDTO();
				operation.setCode(operationCode);
				operation.setName("ALL");
				operations.add(operation);
				continue;
			}
			DataOperationDTO operation = operationMap.get(operationCode);
			if (operation != null) {
				operations.add(operation);
			} else {
				opIter.remove();
			}
		}
		
		List<PropertyRule> propertyRules = JSON.parseArray(propertyRulesStr, PropertyRule.class);
		Iterator<PropertyRule> ruleIter = propertyRules.iterator();
		while (ruleIter.hasNext()) {
			PropertyRule propertyRule = ruleIter.next();
			if (propertyMap.get(propertyRule.getCode()) == null) {
				ruleIter.remove();
			}
		}
		DataGrantRecordDTO dataGrantRecordDTO = new DataGrantRecordDTO();
		BeanUtils.copyProperties(dataGrantRecordDO, dataGrantRecordDTO);
		dataGrantRecordDTO.setModelCode(modelCode);
		dataGrantRecordDTO.setOperationCodes(operationCodes);
		dataGrantRecordDTO.setOperations(operations);
		dataGrantRecordDTO.setPropertyRules(propertyRules);
		return dataGrantRecordDTO;
	} 
	
	private List<DataGrantRecordDTO> buildDataGrantRecordDTOs(List<DataGrantRecordDO> dataGrantRecordDOs, String modelCode,
			Map<String, DataPropertyDTO> propertyMap, Map<String, DataOperationDTO> operationMap){
		List<DataGrantRecordDTO> records = new ArrayList<>();
		if(CollectionUtil.isEmpty(dataGrantRecordDOs)){
			return records;
		}
		for(DataGrantRecordDO dataGrantRecordDO : dataGrantRecordDOs) {
			records.add(this.buildDataGrantRecordDTO(dataGrantRecordDO, modelCode, propertyMap, operationMap));
		}
		return records;
	}
	
	private List<DataGrantRecordDTO> buildDataGrantRecordDTOs(List<DataGrantRecordDO> dataGrantRecordDOs){
		List<DataGrantRecordDTO> records = new ArrayList<>();
		if (CollectionUtils.isEmpty(dataGrantRecordDOs)) {
			return records;
		}
		
		List<Long> modelIds = dataGrantRecordDOs.stream().map(DataGrantRecordDO::getId).distinct().collect(Collectors.toList());
		List<DataModelDTO> models = dataModelManager.getByIds(modelIds);
		Assert.notEmpty(models, "modelCode非法");
		Assert.isTrue(modelIds.size() == models.size(), "modelCode非法.");
		Map<Long, DataModelDTO> modelMap = new HashMap<>();
		Map<Long, Map<String, DataPropertyDTO>> modelPropertyMap = new HashMap<>();
		Map<Long, Map<String, DataOperationDTO>> modelOperationMap = new HashMap<>();
		models.forEach(model -> {
			Long modelId = model.getId();
			modelMap.put(modelId, model);
			modelPropertyMap.put(modelId, this.getPropertyMap(modelId));
			modelOperationMap.put(modelId, this.getOperationMap(modelId));
		});
		for (DataGrantRecordDO dataGrantRecordDO : dataGrantRecordDOs) {
			Long modelId = dataGrantRecordDO.getModelId();
			DataModelDTO dataModelDTO = modelMap.get(modelId);
			String modelCode = dataModelDTO.getCode();
			Map<String, DataPropertyDTO> propertyMap = modelPropertyMap.get(modelId);
			Map<String, DataOperationDTO> operationMap = modelOperationMap.get(modelId);
			records.add(this.buildDataGrantRecordDTO(dataGrantRecordDO, modelCode, propertyMap, operationMap));
		}
		return records;
	}
	
	@Override
	public List<DataGrantRecordDTO> listDataGrantRecord(DataGrantQueryParam dataGrantQueryParam) {
		boolean hierarchicalRole = dataGrantQueryParam.isHierarchicalRole();
		boolean hierarchicalPermissionGroup = dataGrantQueryParam.isHierarchicalPermissionGroup();
		boolean hierarchicalRolePermissionGroup = dataGrantQueryParam.isHierarchicalRolePermissionGroup();
		DataGrantRecordQueryParam dataGrantRecordQueryParam = this.buildDataGrantRecordQueryParam(dataGrantQueryParam);
		String grantTargetId = dataGrantQueryParam.getGrantTargetId();
		String tenant = dataGrantQueryParam.getTenant();
		Long modelId = dataGrantRecordQueryParam.getModelId();
		String modelCode = dataGrantQueryParam.getModelCode();
		String operationCode = dataGrantQueryParam.getOperationCode();
		//此appkey可能跟modelCode的appkey不同
		String appKey = dataGrantQueryParam.getAppKey();
		Assert.hasText(appKey, "appKey不能为空");
		int grantTargetType = dataGrantQueryParam.getGrantTargetType();
		List<DataGrantRecordDO> datas = dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam);
		if(GrantTargetType.USER.getCode() == grantTargetType){
			if(hierarchicalRole){
				//获取到用户拥有的角色
				List<String> roleIds = getUserRoleIds(appKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(roleIds)){
					dataGrantRecordQueryParam.setGrantTargetType(GrantTargetType.ROLE.getCode());
					dataGrantRecordQueryParam.setGrantTargetIds(roleIds);
					List<DataGrantRecordDO> roleDatas = dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam);
					if(CollectionUtil.isNotEmpty(roleDatas)){
						datas.addAll(roleDatas);
					}
				}
			}
			//获取到用户拥有的权限组
			List<String> groupIds = new ArrayList<>();
			if(hierarchicalRolePermissionGroup){
				List<String> list = getUserRolePermissionGroupIds(appKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(list)){
					groupIds.addAll(list);
				}
			}
			if(hierarchicalPermissionGroup){
				List<String> list = getUserPermissionGroupIds(appKey, grantTargetId, tenant);
				if(CollectionUtil.isNotEmpty(list)){
					groupIds.addAll(list);
				}
			}
			if(CollectionUtil.isNotEmpty(groupIds)){
				dataGrantRecordQueryParam.setGrantTargetType(GrantTargetType.PERMISSION_GROUP.getCode());
				dataGrantRecordQueryParam.setGrantTargetIds(groupIds);
				List<DataGrantRecordDO> permissionGroupDatas = dataGrantRecordDAO.listDataGrantRecord(dataGrantRecordQueryParam);
				if(CollectionUtil.isNotEmpty(permissionGroupDatas)){
					datas.addAll(permissionGroupDatas);
				}
			}
		}
		List<DataGrantRecordDTO> dataGrantRecordDTOs = null;
		if (modelId != null) {
			Map<String, DataPropertyDTO> propertyMap = this.getPropertyMap(modelId);
			Map<String, DataOperationDTO> operationMap = this.getOperationMap(modelId);
			dataGrantRecordDTOs = buildDataGrantRecordDTOs(datas, modelCode, propertyMap, operationMap);
			this.filterDataGrantRecords(operationCode, dataGrantQueryParam.getData(), dataGrantRecordDTOs);
		} else {
			dataGrantRecordDTOs = this.buildDataGrantRecordDTOs(datas);
		}
		return dataGrantRecordDTOs;
	}

	@Override
	public PageVO<DataGrantRecordDTO> pageDataGrantRecord(DataGrantQueryParam dataGrantQueryParam) {
		Assert.notNull(dataGrantQueryParam, "参数不能为空");
		PageVO<DataGrantRecordDTO> pageVO = new PageVO<DataGrantRecordDTO>(dataGrantQueryParam);
		DataGrantRecordQueryParam dataGrantRecordQueryParam = this.buildDataGrantRecordQueryParam(dataGrantQueryParam);
		Long modelId = dataGrantRecordQueryParam.getModelId();
		String modelCode = dataGrantQueryParam.getModelCode();
		if(dataGrantQueryParam.isReturnTotalCount()){
    		long total = dataGrantRecordDAO.countDataGrantRecord(dataGrantRecordQueryParam);
    		pageVO.setTotal(total);
    		if(total == 0){
    			return pageVO;
    		}
		}
		List<DataGrantRecordDO> datas = dataGrantRecordDAO.pageDataGrantRecord(dataGrantRecordQueryParam);
		List<DataGrantRecordDTO> dataGrantRecordDTOs = null;
		if (modelId != null) {
			Map<String, DataPropertyDTO> propertyMap = this.getPropertyMap(modelId);
			Map<String, DataOperationDTO> operationMap = this.getOperationMap(modelId);
			dataGrantRecordDTOs = buildDataGrantRecordDTOs(datas, modelCode, propertyMap, operationMap);
		} else {
			dataGrantRecordDTOs = this.buildDataGrantRecordDTOs(datas);
		}
		pageVO.setItems(dataGrantRecordDTOs);
		return pageVO;
	}
	
	private List<String> getUserRoleIds(String appKey, String userId, String tenant){
		List<String> userRoleIds = new ArrayList<String>();
		//查出用户所有的角色
		UserRoleRefQueryParam param = new UserRoleRefQueryParam();
		param.setAppKey(appKey);
		param.setTenant(tenant);
		param.setUserId(userId);
		param.setReturnNotEffective(false);
		List<UserRoleRefDTO> userRoleRefVOs = userRoleRefDAO.listUserRoleRef(param);
		//用户没有角色
		if(CollectionUtil.isEmpty(userRoleRefVOs)){
			return userRoleIds;
		}
		for(UserRoleRefDTO userRoleRefVO : userRoleRefVOs){
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

	@Override
	public boolean isModelUsed(Long modelId) {
		return dataGrantRecordDAO.getOneByModel(modelId) != null;
	}
}
