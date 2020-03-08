package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataGrantRecordDO;
import com.kingbo401.acl.model.entity.param.DataGrantRecordParam;
import com.kingbo401.acl.model.entity.param.DataGrantRecordQueryParam;

public interface DataGrantRecordDAO {
	int create(DataGrantRecordDO dataGrantRecord);
	
	int removeByIds(@Param("ids")List<Long> ids);
	
	int removeById(@Param("id")Long id);
	
	DataGrantRecordDO getById(@Param("id")Long id);
	
	List<DataGrantRecordDO> getByIds(@Param("ids")List<Long> ids);
	
	int removeByParam(DataGrantRecordParam param);
	
	int update(DataGrantRecordDO dataGrantRecord);
	
	List<DataGrantRecordDO> listDataGrantRecord(DataGrantRecordQueryParam param);
	
	List<DataGrantRecordDO> pageDataGrantRecord(DataGrantRecordQueryParam param);
	
	long countDataGrantRecord(DataGrantRecordQueryParam param);
	
	DataGrantRecordDO getOneByModel(@Param("modelId") Long modelId);
}
