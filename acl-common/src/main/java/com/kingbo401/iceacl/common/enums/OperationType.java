package com.kingbo401.iceacl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum OperationType {
	DELETE(0, "删除"), 
	ADD(1, "新增"), 
	UPDATE(2, "修改");

	private static final Map<Integer, OperationType> TYPE_MAP;

	static {
		Map<Integer, OperationType> map = new HashMap<Integer, OperationType>();
		for (OperationType type : OperationType.values()) {
			map.put(type.getCode(), type);
		}
		TYPE_MAP = Collections.unmodifiableMap(map);
	}

	public static boolean isValid(int code) {
		return TYPE_MAP.get(code) != null;
	}

	public static String getName(int code) {
		OperationType userRoleGrantType = TYPE_MAP.get(code);
		if (userRoleGrantType != null) {
			return userRoleGrantType.getName();
		}
		return null;
	}

	private int code;

	private String name;

	OperationType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public Integer getCode() {
		return code;
	}
}
