package com.myunihome.myxapp.web.business.common.model;

import java.io.Serializable;

public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7479989484261550982L;
	
	/**
	 * 菜单名称
	 */
	private String menuName;
	
	/**
	 * 菜单链接
	 */
	private String menuUrl;
	
	/**
	 * 路径类型
	 * absolute为绝对
	 * relative为相对
	 */
	private String urlType;
	
	/**
	 * 菜单标识  
	 * across为横
	 * vertical为竖
	 */
	private String menuFlog;
	
	/**
	 * 菜单样式
	 */
	private String menuClass;
	
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuFlog() {
		return menuFlog;
	}

	public void setMenuFlog(String menuFlog) {
		this.menuFlog = menuFlog;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getMenuClass() {
		return menuClass;
	}

	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}
	
	
}
