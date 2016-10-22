package com.myunihome.myxapp.web.business.oper.model;

import com.myunihome.myxapp.sys.api.sysoper.param.GnOperVO;

public class GnOperInfo extends GnOperVO {
	private static final long serialVersionUID = 1L;
	/**
	 * 组织名称
	 */
	private String orgName;
	
	/**
	 * 员工姓名
	 */
	private String staffName;
	
	/**
	 * 省份
	 */
	private String provinceName;
	
	/**
	 * 城市
	 */
	private String cityName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
