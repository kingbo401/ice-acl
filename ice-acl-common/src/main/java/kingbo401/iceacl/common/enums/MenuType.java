package kingbo401.iceacl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum MenuType {
	PAGE(1, "页面"), 
	TAB(2, "tab"), 
	HIDDEN(3, "hidden");

	private static final Map<Integer, MenuType> TYPE_MAP;

	static {
		Map<Integer, MenuType> map = new HashMap<>();
		for (MenuType type : MenuType.values()) {
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
		MenuType userRoleGrantType = TYPE_MAP.get(code);
		if (userRoleGrantType != null) {
			return userRoleGrantType.getName();
		}
		return null;
	}

	private Integer code;

	private String name;

	MenuType(Integer code, String name) {
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
