package com.kingbo401.iceacl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.iceacl.model.po.DataPropertyPO;
import com.kingbo401.iceacl.model.po.param.DataPropertyQueryParam;

public interface DataPropertyDAO {
	int create(DataPropertyPO dataProperty);

	int update(DataPropertyPO dataProperty);

    int removeById(@Param("id") long id);

	List<DataPropertyPO> getByCodes(@Param("appKey") String appKey,@Param("propertyCodes")List<String> propertyCodes);

	DataPropertyPO getByCode(@Param("appKey")String appKey, @Param("code")String code);

	List<DataPropertyPO> getByIds(@Param("ids")List<Long> ids);

	List<DataPropertyPO> listDataProperty(DataPropertyQueryParam param);

	List<DataPropertyPO> pageDataProperty(DataPropertyQueryParam param);

	int countDataProperty(DataPropertyQueryParam param);
}
