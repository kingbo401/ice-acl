package com.kingbo401.iceacl.common.constant;

public class AclConstant {
	/**
	 * 默认编码方式
	 */
	public static final String DFT_CHARSET = "UTF-8";
	
	/**
	 * 拥有所有值的标识
	 */
	public static final String ALL_DATA = "_a_d";
	/**
	 * 如果授权对象不区分租户，使用此常量给授权参数里的租户字段赋值
	 */
	public static final String TENANT_COMMON = "_t_c";
	
	/**
	 * 删除的状态
	 */
	public static final int STATUS_REMOVE = 0;
	/**
	 * 正常状态
	 */
	public static final int STATUS_NORMAL = 1;
	/**
	 * 封禁状态
	 */
	public static final int STATUS_FREEZE = 2;
	
	
	/**
	 * 属性禁止访问标识
	 */
	public static final int DATA_PROPERTY_ACCESS_FORBIDDEN = 0;
	/**
	 * 属性允许访问标识
	 */
	public static final int DATA_PROPERTY_ACCESS_ALLOW = 1;
	/**
	 * 默认分组
	 */
	public static final String DEF_SUBGROUP = "g1";
}
