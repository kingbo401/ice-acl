package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataPropertyAccessPO;
import com.kingbo401.iceacl.model.po.param.DataPropertyAccessParam;

public interface DataPropertyAccessDAO {
    List<DataPropertyAccessPO> listDataPropertyAccess(DataPropertyAccessParam param);
    
    int create(DataPropertyAccessPO dataPropertyAccess);

    int batchCreate(@Param("datas")List<DataPropertyAccessPO> dataPropertyAccessList);
    
    int updateRefsStatus(DataPropertyAccessParam param);
}
