package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.DataOperationDO;

public interface DataOperationDAO {
	int create(DataOperationDO dataOperation);

	int update(DataOperationDO dataOperation);

	DataOperationDO getOperationByCode(@Param("modelId") long modelId, @Param("code") String code);

	List<DataOperationDO> listDataOperationByModelId(@Param("modelId") long modelId);
}
