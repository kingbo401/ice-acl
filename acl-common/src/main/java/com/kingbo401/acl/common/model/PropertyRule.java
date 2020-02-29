package com.kingbo401.acl.common.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyRule {
	private String code;
	private String name;
	private String comparator;
	private List<ValueTextPair> values;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComparator() {
		return comparator;
	}
	public void setComparator(String comparator) {
		this.comparator = comparator;
	}
	public List<ValueTextPair> getValues() {
		return values;
	}
	public void setValues(List<ValueTextPair> values) {
		this.values = values;
	}
	public void addValue(ValueTextPair value) {
		if (values == null) {
			values = new ArrayList<>();
		}
		values.add(value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyRule other = (PropertyRule) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
