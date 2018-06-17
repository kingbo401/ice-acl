package com.kingbo401.iceacl.manager;

import java.util.List;

import com.kingbo401.iceacl.model.dto.DataPropertyAccessDTO;
import com.kingbo401.iceacl.model.dto.param.DataPropertyCodeAccessParam;

public interface DataPropertyAccessManager {

    boolean createDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean updateDataPropertyControl(DataPropertyCodeAccessParam param);

    boolean removeDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean freezeDataPropertyControl(DataPropertyCodeAccessParam param);
    
    boolean unfreezeDataPropertyControl(DataPropertyCodeAccessParam param);

    List<DataPropertyAccessDTO> listDataPropertyControl(DataPropertyCodeAccessParam param);
}