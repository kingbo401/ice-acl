package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataPropertyAccessDTO;
import com.kingbo401.acl.model.dto.param.DataPropertyCodeAccessParam;

public interface DataPropertyAccessManager {

    boolean createDataPropertyAccess(DataPropertyCodeAccessParam param);
    
    boolean updateDataPropertyAccess(DataPropertyCodeAccessParam param);

    boolean removeDataPropertyAccess(DataPropertyCodeAccessParam param);
    
    boolean freezeDataPropertyAccess(DataPropertyCodeAccessParam param);
    
    boolean unfreezeDataPropertyAccess(DataPropertyCodeAccessParam param);

    List<DataPropertyAccessDTO> listDataPropertyAccess(DataPropertyCodeAccessParam param);
}