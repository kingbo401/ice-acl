package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.DataOperationDTO;

public interface DataOperationManager {
	DataOperationDTO create(DataOperationDTO dataOperationDTO);
	
	DataOperationDTO update(DataOperationDTO dataOperationDTO);
	
	boolean remove(DataOperationDTO dataOperationDTO);
	
	boolean freeze(DataOperationDTO dataOperationDTO);
	
	boolean unfreeze(DataOperationDTO dataOperationDTO);
	
	public DataOperationDTO getByCode(String appKey, String modelCode, String operationCode);
	
	public DataOperationDTO getByCode(Long modelId, String operationCode);
	
	List<DataOperationDTO> listDataOperation(String appKey, String modelCode);
	
	List<DataOperationDTO> listDataOperation(Long modelId);
}
