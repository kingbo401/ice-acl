package com.kingbo401.acl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
   *   授权类型
 * @author kingbo
 *
 */
public enum GrantTargetType {
	/**
	 * 用户
	 */
	USER(1),
	/**
	 * 角色
	 */
	ROLE(2),
	/**
	 * 权限组
	 */
	PERMISSION_GROUP(3),
	;
	
	private static final Map<Integer, GrantTargetType> TYPE_MAP;
	static {
		Map<Integer, GrantTargetType> map = new HashMap<Integer, GrantTargetType>();
		for (GrantTargetType type : GrantTargetType.values()) {
			map.put(type.code, type);
		}
		TYPE_MAP = Collections.unmodifiableMap(map);
	}
	
	public static boolean isValid(int code){
		return TYPE_MAP.get(code) != null;
	}
	
	public static GrantTargetType getTargetType(int code){
		return TYPE_MAP.get(code);
	}

	GrantTargetType(int code){
		this.code = code;
	}
	
	private int code;

	public int getCode() {
		return code;
	}
}
