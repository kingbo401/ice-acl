package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataOperationDTO;
import com.kingbo401.acl.model.dto.param.DataOperationParam;

public interface DataOperationManager {
	DataOperationDTO createDataOperation(DataOperationParam dataOperationParam);
	
	DataOperationDTO updateDataOperation(DataOperationParam dataOperationParam);
	
	boolean removeDataOperation(String appKey, String modelCode, String operationCode);
	
	boolean freezeDataOperation(String appKey, String modelCode, String operationCode);
	
	boolean unfreezeDataOperation(String appKey, String modelCode, String operationCode);
	
	public DataOperationDTO getDataOperation(String appKey, String modelCode, String operationCode);
	
	public DataOperationDTO getDataOperation(Long modelId, String operationCode);
	
	List<DataOperationDTO> listDataOperation(String appKey, String modelCode);
	
	List<DataOperationDTO> listDataOperation(Long modelId);
}
