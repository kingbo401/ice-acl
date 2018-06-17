package kingbo401.iceacl.common.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum OperationType {
	DELETE(0, "删除"), ADD(1, "新增"), UPDATE(2, "修改");

	private static final Map<Integer, OperationType> typeMap;

	static {
		Map<Integer, OperationType> map = new HashMap<Integer, OperationType>();
		for (OperationType type : OperationType.values()) {
			map.put(type.getCode(), type);
		}
		typeMap = Collections.unmodifiableMap(map);
	}

	public static boolean isValid(int code) {
		return typeMap.get(code) != null;
	}

	public static String getName(int code) {
		OperationType userRoleGrantType = typeMap.get(code);
		if (userRoleGrantType != null) {
			return userRoleGrantType.getName();
		}
		return null;
	}

	private int code;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	OperationType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
