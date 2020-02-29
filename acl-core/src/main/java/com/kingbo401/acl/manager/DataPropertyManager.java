package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataPropertyDTO;

public interface DataPropertyManager {
	DataPropertyDTO create(DataPropertyDTO dataPropertyDTO);
	
	DataPropertyDTO update(DataPropertyDTO dataPropertyDTO);
	
	boolean remove(DataPropertyDTO dataPropertyDTO);
	
	boolean freeze(DataPropertyDTO dataPropertyDTO);
	
	boolean unfreeze(DataPropertyDTO dataPropertyDTO);

	List<DataPropertyDTO> getByCodes(Long modelId, List<String> propertyCodes);
	
	DataPropertyDTO getByCode(Long modelId, String propertyCode);
	
	List<DataPropertyDTO> listDataProperty(Long modelId);
}
