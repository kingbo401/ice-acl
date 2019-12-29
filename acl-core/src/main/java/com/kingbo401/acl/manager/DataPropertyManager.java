package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataPropertyDTO;

public interface DataPropertyManager {
	DataPropertyDTO createDataProperty(DataPropertyDTO dataPropertyDTO);
	
	DataPropertyDTO updateDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean removeDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean freezeDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean unfreezeDataProperty(DataPropertyDTO dataPropertyDTO);

	List<DataPropertyDTO> getDataProperties(Long modelId, List<String> propertyCodes);
	
	DataPropertyDTO getDataProperty(Long modelId, String propertyCode);
	
	List<DataPropertyDTO> listDataProperty(Long modelId);
}
