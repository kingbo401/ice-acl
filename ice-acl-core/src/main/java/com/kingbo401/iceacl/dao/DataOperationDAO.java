package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataOperationPO;

public interface DataOperationDAO {
	int create(DataOperationPO dataOperation);

	int update(DataOperationPO dataOperation);

	DataOperationPO getOperationByCode(@Param("modelId") long modelId, @Param("code") String code);

	List<DataOperationPO> listDataOperationByModelId(@Param("modelId") long modelId);
}
