package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataModelDO;
import com.kingbo401.acl.model.entity.param.DataModelQueryParam;

public interface DataModelDAO {
	int create(DataModelDO dataModel);

	int update(DataModelDO dataModel);

	DataModelDO getById(@Param("id") long id);

	DataModelDO getByCode(@Param("code") String code);
	
	DataModelDO getByCode0(@Param("code") String code);

	List<DataModelDO> getByIds(@Param("ids") List<Long> modelIds);
	
	List<DataModelDO> getByCodes(@Param("codes") List<String> modelCodes);

	List<DataModelDO> listModel(DataModelQueryParam dataModelQueryParam);

	List<DataModelDO> pageModel(DataModelQueryParam dataModelQueryParam);

	int countModel(DataModelQueryParam dataModelQueryParam);
}
