package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "data_privilege")
@SuppressWarnings("serial")
public class DataPrivilege extends AbstractBaseModel {
	@Column(name = "emp_cat_priv")
	private String empCatPriv;

	@Column(name = "has_editable")
	private String hasEditable;
	
	@Column(name = "has_deletable")
	private String hasDeletable;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "business_object_id")
	private BusinessObject bo;
	
	@Transient
	private String empCatPrivNames;
	@Transient
	private String[] privilegeArrEmpCat;
	
	/*@Transient
	private String[] privilegeArrEmpCatPayrollReport;
	@Transient
	private String[] privilegeArrEmpCatManageEmployee;*/

	public String getEmpCatPriv() {
		return empCatPriv;
	}

	public void setEmpCatPriv(String empCatPriv) {
		this.empCatPriv = empCatPriv;
	}

	public String getHasEditable() {
		return hasEditable;
	}

	public void setHasEditable(String hasEditable) {
		this.hasEditable = hasEditable;
	}
	
	public String getHasDeletable() {
		return hasDeletable;
	}

	public void setHasDeletable(String hasDeletable) {
		this.hasDeletable = hasDeletable;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public BusinessObject getBo() {
		return bo;
	}

	public void setBo(BusinessObject bo) {
		this.bo = bo;
	}

	/*@Transient
	public String[] getPrivilegeArrEmpCatPayrollReport() {
		return privilegeArrEmpCatPayrollReport;
	}

	@Transient
	public void setPrivilegeArrEmpCatPayrollReport(String[] privilegeArrEmpCatPayrollReport) {
		this.privilegeArrEmpCatPayrollReport = privilegeArrEmpCatPayrollReport;
	}

	@Transient
	public String[] getPrivilegeArrEmpCatManageEmployee() {
		return privilegeArrEmpCatManageEmployee;
	}

	@Transient
	public void setPrivilegeArrEmpCatManageEmployee(String[] privilegeArrEmpCatManageEmployee) {
		this.privilegeArrEmpCatManageEmployee = privilegeArrEmpCatManageEmployee;
	}*/

	@Transient
	public String[] getPrivilegeArrEmpCat() {
		return privilegeArrEmpCat;
	}

	@Transient
	public void setPrivilegeArrEmpCat(String[] privilegeArrEmpCat) {
		this.privilegeArrEmpCat = privilegeArrEmpCat;
	}

	@Transient
	public String getEmpCatPrivNames() {
		return empCatPrivNames;
	}

	@Transient
	public void setEmpCatPrivNames(String empCatPrivNames) {
		this.empCatPrivNames = empCatPrivNames;
	}
}