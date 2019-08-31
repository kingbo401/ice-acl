package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataGrantRecordDetailDO;

public interface DataGrantRecordDetailDAO {
	int batchInsert(@Param("datas")List<DataGrantRecordDetailDO> dataGrantRecordDetails);
	int removeByRecordIds(@Param("recordIds")List<Long> recordIds);
	int removeByRecordId(@Param("recordId")long recordId);
	List<DataGrantRecordDetailDO> listDetailByRecordId(@Param("recordId")long recordId);
	List<DataGrantRecordDetailDO> listDetailByRecordIds(@Param("recordIds")List<Long> recordIds);
}
