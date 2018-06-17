package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.DataModelDO;
import com.kingbo401.iceacl.model.db.DataModelPropertyRefDO;
import com.kingbo401.iceacl.model.db.DataPropertyDO;

public interface DataModelPropertyRefDAO {
	int batchCreate(@Param("list")List<DataModelPropertyRefDO> list);
	
	int updateRefsStatus(@Param("modelId") long modelId, @Param("propertyIds") List<Long> propertyIds, @Param("status")int status);
	
	List<DataModelPropertyRefDO> listRefsByModelId(@Param("modelId") long modelId);

	List<DataPropertyDO> listDataPropertyByModelId(@Param("modelId") long modelId);
	
	List<DataModelDO> listDataModelByPropertyId(@Param("propertyId") long propertyId);
}
