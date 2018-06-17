package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.dto.DataModelPropertyRefDTO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.dto.param.DataModelPropertyInfoRefParam;
import com.kingbo401.iceacl.model.dto.param.DataModelPropertyRefParam;

public interface DataModelPropertyRefManager {
	boolean addDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam);
	boolean addDataModelPropertyInfoRef(DataModelPropertyInfoRefParam dataModelPropertyInfoRefParam);
	boolean removeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam);
	boolean freezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam);
	boolean unfreezeDataModelPropertyRef(DataModelPropertyRefParam dataModelPropertyRefParam);

	
	
	List<DataModelPropertyRefDTO> listDataPropertyRef(long modelId);
	
	List<DataModelPropertyRefDTO> listDataPropertyRef(String appKey, String modelCode);
	List<DataPropertyDTO> listDataProperty(String appKey, String modelCode);
	List<DataPropertyDTO> listDataProperty(long modelId);
	List<DataModelDTO> listDataModel(long propertyId);
}
