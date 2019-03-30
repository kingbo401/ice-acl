package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataGrantRecordPO;
import com.kingbo401.iceacl.model.po.param.DataGrantPropertyValueParam;
import com.kingbo401.iceacl.model.po.param.DataGrantRecordParam;
import com.kingbo401.iceacl.model.po.param.DataGrantRecordQueryParam;

public interface DataGrantRecordDAO {
	int create(DataGrantRecordPO dataGrantRecord);
	
	int removeByIds(@Param("ids")List<Long> ids);
	
	int removeById(@Param("id")long id);
	
	DataGrantRecordPO getById(@Param("id")long id);
	
	List<DataGrantRecordPO> getByIds(@Param("ids")List<Long> ids);
	
	int removeByParam(DataGrantRecordParam param);
	
	int update(DataGrantRecordPO dataGrantRecord);
	
	List<DataGrantRecordPO> listDataGrantRecord(DataGrantRecordQueryParam param);
	
	List<DataGrantRecordPO> pageDataGrantRecord(DataGrantRecordQueryParam param);
	
	long countDataGrantRecord(DataGrantRecordQueryParam param);
	
	List<Long> listIdByPropertyValues(DataGrantPropertyValueParam param);
	
	List<Long> pageIdByPropertyValues(DataGrantPropertyValueParam param);
	
	long countIdByPropertyValues(DataGrantPropertyValueParam param);
	
	DataGrantRecordPO getOneDataGrantRecord(@Param("modelId")Long modelId, @Param("operationId")Long operationId, @Param("propertyId")Long propertyId);

}
