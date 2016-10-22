package com.myunihome.myxapp.web.business.common.model;

import java.util.List;

public class MenuVo {
	
	/**
	 * 菜单标识
	 */
	private long menuId;

	/**
	 * 菜单名称
	 */
    private String menuName;

	/**
	 * 菜单描述
	 */
    private String menuDesc;
    
    /**
     * 上级菜单
     */
    private long menuPid;

    /**
     * 菜单URL
     */
    private String menuUrl;
    
    private List<MenuVo> menuList;
    
    private String  menuPic;

	public String getMenuPic() {
		return menuPic;
	}


	public void setMenuPic(String menuPic) {
		this.menuPic = menuPic;
	}

	public long getMenuId() {
		return menuId;
	}


	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}


	public String getMenuName() {
		return menuName;
	}


	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}


	public String getMenuDesc() {
		return menuDesc;
	}


	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}


	public long getMenuPid() {
		return menuPid;
	}


	public void setMenuPid(long menuPid) {
		this.menuPid = menuPid;
	}


	public String getMenuUrl() {
		return menuUrl;
	}


	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}


	public List<MenuVo> getMenuList() {
		return menuList;
	}


	public void setMenuList(List<MenuVo> menuList) {
		this.menuList = menuList;
	}

}
