package com.myunihome.myxapp.web.business.operarea.model;

import com.myunihome.myxapp.sys.api.sysoperarea.param.GnOperAreaVO;

public class GnOperAreaInfo extends GnOperAreaVO{

	private static final long serialVersionUID = 1L;
	
	private String areaName;
	
	private String addressDesc;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddressDesc() {
		return addressDesc;
	}

	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}
}
