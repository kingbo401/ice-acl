package com.kingbo401.iceacl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum MenuShowType {
	PAGE(1, "page"), 
	TAB(2, "tab"), 
	HIDDEN(3, "hidden");

	private static final Map<Integer, MenuShowType> TYPE_MAP;

	static {
		Map<Integer, MenuShowType> map = new HashMap<>();
		for (MenuShowType type : MenuShowType.values()) {
			map.put(type.getCode(), type);
		}
		TYPE_MAP = Collections.unmodifiableMap(map);
	}

	public static boolean isValid(Integer code) {
		return TYPE_MAP.get(code) != null;
	}

	public static String getName(Integer code) {
		if(code == null){
			return null;
		}
		MenuShowType userRoleGrantType = TYPE_MAP.get(code);
		if (userRoleGrantType != null) {
			return userRoleGrantType.getName();
		}
		return null;
	}

	private Integer code;

	private String name;

	MenuShowType(Integer code, String name) {
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
