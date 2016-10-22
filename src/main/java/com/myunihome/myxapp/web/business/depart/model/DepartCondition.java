package com.myunihome.myxapp.web.business.depart.model;

import java.io.Serializable;

public class DepartCondition implements Serializable {

    private static final long serialVersionUID = 1L;

	private String departName;

    private String parentDepartId;

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getParentDepartId() {
        return parentDepartId;
    }

    public void setParentDepartId(String parentDepartId) {
        this.parentDepartId = parentDepartId;
    }
}
