package kingbo401.iceacl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum GrantTargetType {
	USER(1),//用户
	ROLE(2),//角色
	PERMISSION_GROUP(3),//权限组
	;
	
	private static final Map<Integer, GrantTargetType> typeMap;
	static {
		Map<Integer, GrantTargetType> map = new HashMap<Integer, GrantTargetType>();
		for (GrantTargetType type : GrantTargetType.values()) {
			map.put(type.code, type);
		}
		typeMap = Collections.unmodifiableMap(map);
	}
	
	public static boolean isValid(int code){
		return typeMap.get(code) != null;
	}
	
	public static GrantTargetType getTargetType(int code){
		return typeMap.get(code);
	}
	
	private int code;

	GrantTargetType(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
