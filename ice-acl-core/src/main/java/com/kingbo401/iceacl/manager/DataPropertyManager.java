package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.DataPropertyDTO;
import com.kingbo401.iceacl.model.po.param.DataPropertyQueryParam;

public interface DataPropertyManager {
	DataPropertyDTO createDataProperty(DataPropertyDTO dataPropertyDTO);
	
	DataPropertyDTO updateDataProperty(DataPropertyDTO dataPropertyDTO);
	
	boolean removeDataProperty(String appKey, String propertyCode);
	
	boolean freezeDataProperty(String appKey, String propertyCode);
	
	boolean unfreezeDataProperty(String appKey, String propertyCode);

	List<DataPropertyDTO> getDataProperties(List<Long> ids);

	List<DataPropertyDTO> getDataProperties(String appKey, List<String> propertyCodes);
	
	DataPropertyDTO getDataProperty(String appKey, String propertyCode);
	
	List<DataPropertyDTO> listDataProperty(DataPropertyQueryParam param);
	
	PageVO<DataPropertyDTO> pageDataProperty(DataPropertyQueryParam param);
}
