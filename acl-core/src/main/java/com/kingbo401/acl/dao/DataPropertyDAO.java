package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataPropertyDO;

public interface DataPropertyDAO {
	int create(DataPropertyDO dataProperty);

	int update(DataPropertyDO dataProperty);

    int removeById(@Param("id") Long id);

	List<DataPropertyDO> getByCodes(@Param("modelId") Long modelId, @Param("propertyCodes")List<String> propertyCodes);

	DataPropertyDO getByCode(@Param("modelId") Long modelId, @Param("code")String code);
	
	DataPropertyDO getByCode0(@Param("modelId") Long modelId, @Param("code")String code);

	List<DataPropertyDO> listDataProperty(@Param("modelId") Long modelId);
}
