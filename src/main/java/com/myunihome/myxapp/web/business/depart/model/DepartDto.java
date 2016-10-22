package com.myunihome.myxapp.web.business.depart.model;

import java.io.Serializable;

public class DepartDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String departName;
	private String departKindType;
	private String parentDepartId;
	private String provinceCode;
	private String cityCode;
	private String contactName;
	private String contactTel;
	private String address;
	private String postcode;
	private String areaCode;

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getDepartKindType() {
		return departKindType;
	}

	public void setDepartKindType(String departKindType) {
		this.departKindType = departKindType;
	}

	public String getParentDepartId() {
		return parentDepartId;
	}

	public void setParentDepartId(String parentDepartId) {
		this.parentDepartId = parentDepartId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}
