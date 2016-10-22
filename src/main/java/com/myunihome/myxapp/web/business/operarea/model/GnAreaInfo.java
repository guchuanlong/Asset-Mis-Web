package com.myunihome.myxapp.web.business.operarea.model;

import com.myunihome.myxapp.common.api.area.param.GnAreaVo;

public class GnAreaInfo extends GnAreaVo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 省份名称
	 */
	private String provinceName;
	
	/**
	 * 城市名称
	 */
	private String cityName;
	
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

}
