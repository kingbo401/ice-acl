package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataPropertyAccessDO;
import com.kingbo401.acl.model.entity.param.DataPropertyAccessParam;

public interface DataPropertyAccessDAO {
    List<DataPropertyAccessDO> listDataPropertyAccess(DataPropertyAccessParam param);
    
    int create(DataPropertyAccessDO dataPropertyAccess);

    int batchCreate(@Param("datas")List<DataPropertyAccessDO> dataPropertyAccessList);
    
    int updateRefsStatus(DataPropertyAccessParam param);
}
