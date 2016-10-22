package com.myunihome.myxapp.web.business.staff.model;

import java.io.Serializable;

/**
 * Created by astraea on 2015/8/3.
 */
public class StaffCondition implements Serializable {
	private static final long serialVersionUID = 1L;
	private String staffNo;
	private String staffName;
	private String departId;

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}
}
