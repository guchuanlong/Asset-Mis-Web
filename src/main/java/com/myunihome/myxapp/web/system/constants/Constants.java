package com.myunihome.myxapp.web.system.constants;

public final class Constants {

	private Constants() {
	}

	/**
	 * 用户信息session key
	 */
	public static final String SESSION_USER_KEY = "user_session_key";

	/**
	 * 系统id
	 */
	public static final String SYSTEM_ID = "runner-bis-office";
	
	/**
	 * 系统模块id
	 */
	public static final String SYSTEM_MODEL_ID = "YWSL";

	/**
	 * 生效状态
	 */
	public final static String STATE_NORMAL = "01";
	/**
	 * 注销状态
	 */
	public final static String STATE_DELETED = "02";
	
	/**
	 * 前台创建
	 */
	public final static String CREAT_TYPE_SELF = "1";
	
	/**
	 * 接口同步创建
	 */
	public final static String CREAT_TYPE_OTHER = "0";
	
	public final static String ALL_TENANT = "ALL";

}
