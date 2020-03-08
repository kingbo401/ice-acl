package com.kingbo401.acl.manager;

import java.util.List;

import com.kingbo401.acl.common.model.dto.DataPropertyAccessDTO;
import com.kingbo401.acl.common.model.dto.param.DataPropertyCodeAccessParam;

public interface DataPropertyAccessManager {

    boolean create(DataPropertyCodeAccessParam param);
    
    boolean update(DataPropertyCodeAccessParam param);

    boolean remove(DataPropertyCodeAccessParam param);
    
    boolean freeze(DataPropertyCodeAccessParam param);
    
    boolean unfreeze(DataPropertyCodeAccessParam param);

    List<DataPropertyAccessDTO> listDataPropertyAccess(DataPropertyCodeAccessParam param);
}