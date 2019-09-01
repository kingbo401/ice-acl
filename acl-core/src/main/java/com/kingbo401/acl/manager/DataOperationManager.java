package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataOperationDTO;

public interface DataOperationManager {
	DataOperationDTO createDataOperation(DataOperationDTO dataOperationDTO);
	
	DataOperationDTO updateDataOperation(DataOperationDTO dataOperationDTO);
	
	boolean removeDataOperation(DataOperationDTO dataOperationDTO);
	
	boolean freezeDataOperation(DataOperationDTO dataOperationDTO);
	
	boolean unfreezeDataOperation(DataOperationDTO dataOperationDTO);
	
	public DataOperationDTO getDataOperation(String appKey, String modelCode, String operationCode);
	
	public DataOperationDTO getDataOperation(Long modelId, String operationCode);
	
	List<DataOperationDTO> listDataOperation(String appKey, String modelCode);
	
	List<DataOperationDTO> listDataOperation(Long modelId);
}
