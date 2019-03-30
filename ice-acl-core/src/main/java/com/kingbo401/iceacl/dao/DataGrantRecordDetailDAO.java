package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataGrantRecordDetailPO;

public interface DataGrantRecordDetailDAO {
	int batchInsert(@Param("datas")List<DataGrantRecordDetailPO> dataGrantRecordDetails);
	int removeByRecordIds(@Param("recordIds")List<Long> recordIds);
	int removeByRecordId(@Param("recordId")long recordId);
	List<DataGrantRecordDetailPO> listDetailByRecordId(@Param("recordId")long recordId);
	List<DataGrantRecordDetailPO> listDetailByRecordIds(@Param("recordIds")List<Long> recordIds);
}
