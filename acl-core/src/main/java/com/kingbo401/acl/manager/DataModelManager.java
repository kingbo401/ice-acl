package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.DataModelDTO;
import com.kingbo401.acl.model.entity.param.DataModelQueryParam;
import com.kingbo401.commons.model.PageVO;

public interface DataModelManager {
	DataModelDTO create(DataModelDTO dataModel);
	
	DataModelDTO update(DataModelDTO dataModel);
	
	boolean remove(DataModelDTO dataModel);
	
	boolean freeze(DataModelDTO dataModel);
	
	boolean unfreeze(DataModelDTO dataModel);
	
	DataModelDTO getById(Long id);
	
	DataModelDTO getByCode(String modelCode);
	
	DataModelDTO getByCode(String appKey, String modelCode);
	
	List<DataModelDTO> listDataModel(DataModelQueryParam dataModelQueryParam);
	
	PageVO<DataModelDTO> pageDataModel(DataModelQueryParam dataModelQueryParam);
}
