package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.db.DataPropertyAccessDO;
import com.kingbo401.iceacl.model.db.param.DataPropertyAccessParam;

public interface DataPropertyAccessDAO {
    List<DataPropertyAccessDO> listDataPropertyAccess(DataPropertyAccessParam param);
    
    int create(DataPropertyAccessDO dataPropertyAccessDO);

    int batchCreate(@Param("datas")List<DataPropertyAccessDO> dataPropertyAccessDOs);
    
    int updateRefsStatus(DataPropertyAccessParam param);
}
