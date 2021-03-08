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
	@Transient
	public static final String DATA_TYPE_EMP_CAT = "EMP_CAT";
	
	@Column(name = "data_type")
	private String dataType;
	
	@Column(name = "privilege")
	private String privilege;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@ManyToOne
	@JoinColumn(name = "business_object_id")
	private BusinessObject bo;
	
	@Transient
	private String[] privilegeArrEmpCatPayrollReport;
	@Transient
	private String[] privilegeArrEmpCatManageEmployee;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
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

	@Transient
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
	}
}