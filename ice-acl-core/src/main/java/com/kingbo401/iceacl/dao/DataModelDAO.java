package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataModelPO;
import com.kingbo401.iceacl.model.po.param.DataModelQueryParam;

public interface DataModelDAO {
	int create(DataModelPO dataModel);

	int update(DataModelPO dataModel);

	DataModelPO getModelById(@Param("id") long id);

	DataModelPO getModelByCode(@Param("modelCode") String modelCode);

	List<DataModelPO> getModelsByIds(@Param("ids") List<Long> modelIds);

	List<DataModelPO> listDataModel(DataModelQueryParam dataModelQueryParam);

	List<DataModelPO> pageDataModel(DataModelQueryParam dataModelQueryParam);

	int countDataModel(DataModelQueryParam dataModelQueryParam);
}
