package com.primovision.lutransport.model.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.ReportModel;


@Entity
@Table(name="trailer_net_report")
public class TrailerNetReport extends AbstractBaseModel implements ReportModel {		
	
	@Column(name="employee_company_id")
	protected Long employeeCompanyId;
	
	@Column(name="employee_company_name")
	protected String employeeCompanyName;
	
	@Column(name="active_flag")
	protected char active_flag;
	
	@Column(name="unitNum")
	protected String unitNum;
	
	
	@Column(name = "unload_date")
	protected Date unloadDate;
	
	@Column(name="revenue_amount")
	protected Double revenueAmount=0.0;
	
	@Column(name="driver_pay_amount")
	protected Double driverPayAmount=0.0;
	
	@Column(name="net_amount")
	protected Double NetAmount=0.0;

	public Long getEmployeeCompanyId() {
		return employeeCompanyId;
	}

	public void setEmployeeCompanyId(Long employeeCompanyId) {
		this.employeeCompanyId = employeeCompanyId;
	}

	public String getEmployeeCompanyName() {
		return employeeCompanyName;
	}

	public void setEmployeeCompanyName(String employeeCompanyName) {
		this.employeeCompanyName = employeeCompanyName;
	}

	public String getUnitNum() {
		return unitNum;
	}

	public void setUnitNum(String unitNum) {
		this.unitNum = unitNum;
	}

	public Date getUnloadDate() {
		return unloadDate;
	}

	public void setUnloadDate(Date unloadDate) {
		this.unloadDate = unloadDate;
	}

	public Double getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(Double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

	public Double getDriverPayAmount() {
		return driverPayAmount;
	}

	public void setDriverPayAmount(Double driverPayAmount) {
		this.driverPayAmount = driverPayAmount;
	}

	public Double getNetAmount() {
		return NetAmount;
	}

	public void setNetAmount(Double netAmount) {
		NetAmount = netAmount;
	}
	
	public char getActive_flag() {
		return active_flag;
	}
	
	public void setActive_flag(char active_flag) {
		this.active_flag = active_flag;
	}
	
	
}
