package com.kingbo401.acl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingbo401.acl.model.entity.DataPropertyDO;
import com.kingbo401.acl.model.entity.param.DataPropertyQueryParam;

public interface DataPropertyDAO {
	int create(DataPropertyDO dataProperty);

	int update(DataPropertyDO dataProperty);

    int removeById(@Param("id") long id);

	List<DataPropertyDO> getByCodes(@Param("appKey") String appKey,@Param("propertyCodes")List<String> propertyCodes);

	DataPropertyDO getByCode(@Param("appKey")String appKey, @Param("code")String code);

	List<DataPropertyDO> getByIds(@Param("ids")List<Long> ids);

	List<DataPropertyDO> listDataProperty(DataPropertyQueryParam param);

	List<DataPropertyDO> pageDataProperty(DataPropertyQueryParam param);

	int countDataProperty(DataPropertyQueryParam param);
}
