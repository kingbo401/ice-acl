package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataModelPO;
import com.kingbo401.iceacl.model.po.DataModelPropertyRefPO;
import com.kingbo401.iceacl.model.po.DataPropertyPO;

public interface DataModelPropertyRefDAO {
	int batchCreate(@Param("list")List<DataModelPropertyRefPO> list);
	
	int updateRefsStatus(@Param("modelId") long modelId, @Param("propertyIds") List<Long> propertyIds, @Param("status")int status);
	
	List<DataModelPropertyRefPO> listRefsByModelId(@Param("modelId") long modelId);

	List<DataPropertyPO> listDataPropertyByModelId(@Param("modelId") long modelId);
	
	List<DataModelPO> listDataModelByPropertyId(@Param("propertyId") long propertyId);
}
