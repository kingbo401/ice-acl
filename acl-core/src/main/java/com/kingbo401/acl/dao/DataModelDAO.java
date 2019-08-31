package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataModelDO;
import com.kingbo401.acl.model.entity.param.DataModelQueryParam;

public interface DataModelDAO {
	int create(DataModelDO dataModel);

	int update(DataModelDO dataModel);

	DataModelDO getModelById(@Param("id") long id);

	DataModelDO getModelByCode(@Param("code") String ccode);

	List<DataModelDO> getModelsByIds(@Param("ids") List<Long> modelIds);

	List<DataModelDO> listDataModel(DataModelQueryParam dataModelQueryParam);

	List<DataModelDO> pageDataModel(DataModelQueryParam dataModelQueryParam);

	int countDataModel(DataModelQueryParam dataModelQueryParam);
}
