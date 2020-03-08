package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.DataGrantRecordDTO;
import com.kingbo401.acl.common.model.dto.DatasCheckResult;
import com.kingbo401.acl.common.model.dto.param.DataCheckParam;
import com.kingbo401.acl.common.model.dto.param.DataGrantParam;
import com.kingbo401.acl.common.model.dto.param.DataGrantQueryParam;
import com.kingbo401.acl.common.model.dto.param.DataRevokeParam;
import com.kingbo401.acl.common.model.dto.param.DatasCheckParam;
import com.kingbo401.commons.model.PageVO;

/**
 * 数据权限
 * @author kingbo
 *
 */
public interface DataGrantManager {
	boolean grantDataPermission(DataGrantParam dataGrantParam);
	
	boolean revokeDataPermission(DataRevokeParam dataRevokeParam);
	
	boolean checkDataPermission(DataCheckParam dataCheckParam);
	
	DatasCheckResult checkDatasPermission(DatasCheckParam datasCheckParam);
	
	List<DataGrantRecordDTO> listDataGrantRecord(DataGrantQueryParam dataGrantQueryParam);
	
	PageVO<DataGrantRecordDTO> pageDataGrantRecord(DataGrantQueryParam dataGrantQueryParam);
	
	boolean isModelUsed(Long modelId);
}
