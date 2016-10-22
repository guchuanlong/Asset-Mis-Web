package com.myunihome.myxapp.web.business.oper.model;

import com.myunihome.myxapp.base.vo.BaseInfo;

public class GnStaffOperParams extends BaseInfo{

	private static final long serialVersionUID = 1L;
	
	private String staffName;
	
	private String operCode;
	
	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
}
