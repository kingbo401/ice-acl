package com.kingbo401.iceacl.model.db.param;

import java.util.List;
import java.util.Map;

public class DataGrantPropertyValueParam extends DataGrantRecordQueryParam{
	
	private Map<Long, List<String>> propertyValuesMap;

	public Map<Long, List<String>> getPropertyValuesMap() {
		return propertyValuesMap;
	}

	public void setPropertyValuesMap(Map<Long, List<String>> propertyValuesMap) {
		this.propertyValuesMap = propertyValuesMap;
	}

	public int getPropertyNum(){
		return propertyValuesMap == null ? 0 : propertyValuesMap.size();
	}
}
