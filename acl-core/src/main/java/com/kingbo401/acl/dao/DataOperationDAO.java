package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataOperationDO;

public interface DataOperationDAO {
	int create(DataOperationDO dataOperation);

	int update(DataOperationDO dataOperation);

	DataOperationDO getByCode(@Param("modelId") long modelId, @Param("code") String code);
	
	DataOperationDO getByCode0(@Param("modelId") long modelId, @Param("code") String code);

	List<DataOperationDO> listByModelId(@Param("modelId") long modelId);
}
