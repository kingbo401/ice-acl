package com.kingbo401.acl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
   *    比较符合
 * @author kingbo
 *
 */
public enum Comparator {
	EQ("eq")
	;
	
	private String code;
	
	private static final Map<String, Comparator> TYPE_MAP;
	static {
		Map<String, Comparator> map = new HashMap<>();
		for (Comparator type : Comparator.values()) {
			map.put(type.code, type);
		}
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public static boolean isValid(String code){
		return TYPE_MAP.get(code) != null;
	}
	
	public boolean isMe(String code){
		Comparator comparator = TYPE_MAP.get(code);
		if (comparator == null) {
			return false;
		}
		return this.equals(comparator);
	}
	
	private Comparator(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
