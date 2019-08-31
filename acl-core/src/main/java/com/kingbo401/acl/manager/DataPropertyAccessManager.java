package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.model.dto.DataPropertyAccessDTO;
import com.kingbo401.acl.model.dto.param.DataPropertyCodeAccessParam;

public interface DataPropertyAccessManager {

    boolean createDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean updateDataPropertyControl(DataPropertyCodeAccessParam param);

    boolean removeDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean freezeDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean unfreezeDataPropertyControl(DataPropertyCodeAccessParam param);

    List<DataPropertyAccessDTO> listDataPropertyControl(DataPropertyCodeAccessParam param);
}