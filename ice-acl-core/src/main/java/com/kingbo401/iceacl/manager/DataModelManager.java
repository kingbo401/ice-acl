package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.DataModelDTO;
import com.kingbo401.iceacl.model.po.param.DataModelQueryParam;

public interface DataModelManager {
	DataModelDTO createDataModel(DataModelDTO dataModel);
	
	DataModelDTO updateDataModel(DataModelDTO dataModel);
	
	boolean removeDataModel(String appKey, String modelCode);
	
	boolean freezeDataModel(String appKey, String modelCode);
	
	boolean unfreezeDataModel(String appKey, String modelCode);
	
	DataModelDTO getDataModel(Long id);
	
	DataModelDTO getDataModel(String modelCode);
	
	DataModelDTO getDataModel(String appKey, String modelCode);
	
	List<DataModelDTO> listDataModel(DataModelQueryParam dataModelQueryParam);
	
	PageVO<DataModelDTO> pageDataModel(DataModelQueryParam dataModelQueryParam);
}
