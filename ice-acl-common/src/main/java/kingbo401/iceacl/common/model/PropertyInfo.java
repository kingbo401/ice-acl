package kingbo401.iceacl.common.model;

import com.kingbo401.commons.model.BasePojo;

public class PropertyInfo extends BasePojo{
	private String propertyCode;
	private Integer defaultAccessControl;
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public Integer getDefaultAccessControl() {
		return defaultAccessControl;
	}
	public void setDefaultAccessControl(Integer defaultAccessControl) {
		this.defaultAccessControl = defaultAccessControl;
	}
}
