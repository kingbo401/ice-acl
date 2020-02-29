package com.kingbo401.acl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
   *  数据类型
 * @author kingbo
 *
 */
public enum DataType {
	STRING("string"),
	INT("int"),
	FLOAT("float"),
	BOOLEAN("boolean"),
	DATETIME("datetime"),
	;
	
	private String code;
	
	private static final Map<String, DataType> TYPE_MAP;
	static {
		Map<String, DataType> map = new HashMap<>();
		for (DataType type : DataType.values()) {
			map.put(type.code, type);
		}
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public static boolean isValid(String code){
		return TYPE_MAP.get(code) != null;
	}
	
	public boolean isMe(String code){
		DataType DataType = TYPE_MAP.get(code);
		if (DataType == null) {
			return false;
		}
		return this.equals(DataType);
	}
	
	private DataType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
