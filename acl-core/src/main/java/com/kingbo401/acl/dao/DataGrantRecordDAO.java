package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataGrantRecordDO;
import com.kingbo401.acl.model.entity.param.DataGrantPropertyValueParam;
import com.kingbo401.acl.model.entity.param.DataGrantRecordParam;
import com.kingbo401.acl.model.entity.param.DataGrantRecordQueryParam;

public interface DataGrantRecordDAO {
	int create(DataGrantRecordDO dataGrantRecord);
	
	int removeByIds(@Param("ids")List<Long> ids);
	
	int removeById(@Param("id")long id);
	
	DataGrantRecordDO getById(@Param("id")long id);
	
	List<DataGrantRecordDO> getByIds(@Param("ids")List<Long> ids);
	
	int removeByParam(DataGrantRecordParam param);
	
	int update(DataGrantRecordDO dataGrantRecord);
	
	List<DataGrantRecordDO> listDataGrantRecord(DataGrantRecordQueryParam param);
	
	List<DataGrantRecordDO> pageDataGrantRecord(DataGrantRecordQueryParam param);
	
	long countDataGrantRecord(DataGrantRecordQueryParam param);
	
	List<Long> listIdByPropertyValues(DataGrantPropertyValueParam param);
	
	List<Long> pageIdByPropertyValues(DataGrantPropertyValueParam param);
	
	long countIdByPropertyValues(DataGrantPropertyValueParam param);
	
	DataGrantRecordDO getOneDataGrantRecord(@Param("modelId")Long modelId, @Param("operationId")Long operationId, @Param("propertyId")Long propertyId);

}
