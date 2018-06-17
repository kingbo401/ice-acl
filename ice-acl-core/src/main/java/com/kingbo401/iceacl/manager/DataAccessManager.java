package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.commons.model.PageVO;
import com.kingbo401.iceacl.model.dto.DataGrantRecordDTO;
import com.kingbo401.iceacl.model.dto.DatasCheckResult;
import com.kingbo401.iceacl.model.dto.param.DataCheckParam;
import com.kingbo401.iceacl.model.dto.param.DataGrantParam;
import com.kingbo401.iceacl.model.dto.param.DataGrantQueryParam;
import com.kingbo401.iceacl.model.dto.param.DataRevokeParam;
import com.kingbo401.iceacl.model.dto.param.DatasCheckParam;

public interface DataAccessManager {
	boolean grantDataPermission(DataGrantParam dataGrantParam);
	
	boolean revokeDataPermission(DataRevokeParam dataRevokeParam);
	
	boolean checkDataPermission(DataCheckParam dataCheckParam);
	
	DatasCheckResult checkDatasPermission(DatasCheckParam datasCheckParam);
	
	List<DataGrantRecordDTO> listDataGrantRecord(DataGrantQueryParam dataGrantQueryParam);
	
	PageVO<DataGrantRecordDTO> pageDataGrantRecord(DataGrantQueryParam dataGrantQueryParam);
}
